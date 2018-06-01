#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/time.h>
#include <sys/ioctl.h>
#include <time.h>
#include <string.h>

#define RUN_TIME 30
#define SLEEP_TIME 3
#define BUFFER_SIZE 100
#define READ_END 0
#define WRITE_END 1
#define NUM_CHILD 5

//Creates random sleep time of 0, 1, or 2
int sleepTime() {
    return rand() % SLEEP_TIME;
}

double startTime; // the time the forking starts

//Calculate the time it took forking
double getElapseTime() {
    struct timeval now;
    gettimeofday( & now, NULL);
    double currentTime = (now.tv_sec) * 1000 + (now.tv_usec) / 1000;
    return (currentTime - startTime) / 1000;
}

int main(void) {
    //RunTime Timer
    struct timeval start;

    FILE *fp; //File pointer
    
    gettimeofday(&start, NULL);
    startTime = (start.tv_sec) * 1000 + (start.tv_usec) / 1000;
    
    //Write Buffers for Piping
    char write_msg[BUFFER_SIZE] = "";
    char read_msg[BUFFER_SIZE] = "";
    
    pid_t pid; // child process id
    int index = -1;
    int fd[NUM_CHILD][2]; // file descriptors for the pipe
    
    fd_set inputs, inputfds; // Set array of file descriptors
    struct timeval timeout;
    
    FD_ZERO( & inputs); // Initialize to empty set
    
    //Opening the file
    //fp = fopen("output.txt", "w");
    fp = fopen("output.txt", "w");
    
    // Create pipe for each process
    for (int i = 0; i < NUM_CHILD; i++) {
        // Create the pipe.
        if (pipe(fd[i]) == -1) {
            fprintf(stderr, "pipe() failed");
            return 1;
        }
    }
    
    // Create 5 child processes
    for (int i = 0; i < NUM_CHILD; i++) {
        // Fork a child process.
        pid = fork();
        if (pid > 0) {
            //parent add read end to fd set
            FD_SET(fd[i][READ_END], & inputs);
            //parent close the write end
            close(fd[i][WRITE_END]);
            continue;
        } else if (pid == 0) {
            index = i + 1;
            break;
        } else {
            printf("Error using fork()\n");
            return 1;
        }
    }
    
    if (pid > 0) {
        // PARENT PROCESS.
        while (RUN_TIME > getElapseTime()) {
            int result; //result of selected
            inputfds = inputs; //reset the file descriptors
            
            // 2.5 seconds.
            timeout.tv_sec = 2;
            timeout.tv_usec = 500000;
            
            //Get select() results
            result = select(FD_SETSIZE, & inputfds, NULL, NULL, & timeout);
            
            // Check the results.
            //   No input:  the program loops again.
            //   Got input: print what was typed, then terminate.
            //   Error:     terminate.
            switch (result) {
                case 0:
                { // timeout w/o input
                    fflush(stdout);
                    break;
                }
                    
                case -1:
                { // error
                    perror("Select() failed");
                    return 1;
                }
                    
                    // If, during the wait, we have some action on the file descriptor,
                    // we read the input on stdin and echo it whenever an
                    // <end of line> character is received, until that input is Ctrl-D.
                default:
                { // Got input
                    for (int j = 0; j < NUM_CHILD; j++) {
                        
                        //if file descriptor in array
                        if (FD_ISSET(fd[j][READ_END], & inputfds)) {
                            
                            //read from end to msg buffer
                            if (read(fd[j][READ_END], read_msg, BUFFER_SIZE)) {
                                double p_sec = getElapseTime();
                                double p_min = 0;
                                
                                while (p_sec >= 60) {
                                    p_min++;
                                    p_sec -= 60;
                                }
                                //print out read_msg buffer
                                char message[BUFFER_SIZE * 2];
                                sprintf(message, "%01.0f:%06.3lf: %s \n", p_min, p_sec, read_msg);
                                fputs(message, fp);
                            }
                        }
                    }
                    break;
                }
            }
        } // End while loop
        
        //parent closes read end after looping
        for (int i = 0; i < NUM_CHILD; i++) {
            close(fd[i][READ_END]);
        }
        
        fclose(fp); // closes the file
    } else {
        // CHILD PROCESS
        
        //close read end
        for (int i = 0; i < NUM_CHILD; i++) {
            close(fd[i][READ_END]);
        }
        
        //seed differently for each child sleep time;
        srand(index);
        
        for (int k = 1; RUN_TIME > getElapseTime(); k++) {
            
            double sec = getElapseTime();
            double min = 0;
            
            while (sec >= 60) {
                min++;
                sec -= 60;
            }
            
            if (index == 5) {
                printf("Write to pipe: ");
                
                char input[BUFFER_SIZE];
                gets(input);
                
                sprintf(write_msg, "%01.0f:%06.3lf: Child %d message %s ", min, sec, index, input);
                
                //write a message to pipe
                write(fd[index - 1][WRITE_END], write_msg, BUFFER_SIZE);
                
            } else {
                //message to be written, which Child, which message
                sprintf(write_msg, "%01.0f:%06.3lf: Child %d message %d", min, sec, index, k);
                
                //write a message to pipe
                write(fd[index - 1][WRITE_END], write_msg, BUFFER_SIZE);
                
                //sleep 0,1, or 2 seconds
                sleep(sleepTime());
            }
        }
        
        //close read end
        close(fd[index - 1][WRITE_END]);
    }
    
    return 0;
}