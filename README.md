# gateway
Gateway of MSP clients

# Docker image

First of all, to create a docker image, you will need a jar file of your service.
Getting it is easy enough.

1. You need to write the next in the gradle console:
```./gradlew build``` or use the interface of your IDE to start creating a file.

This command will create a jar file in the /build/libs directory of the project.

2. Next step, you need to go to the root directory where your  project with **Dockerfile** is located.

In the console (terminal), you need to run the command: ```docker build -t gateway .```Where:
- `build` — Build an image from a Dockerfile.
- `-t` — Name in the 'name:tag' format.
- `gateway` — The name of our image.
- `.` — The path to the directory where we are looking for **Dockerfile**(in our case, this is the current directory where we are located).

Our image is ready. You can run the command `docker images` and see your image

3. All that remains is to run the image. You can do this with the following command: `docker run gateway`.

Your image will start. You can view the running images:`docker ps`

You can stop the docker like this: `docker stop <CONTAINER ID>`
