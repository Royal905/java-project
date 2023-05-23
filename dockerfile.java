import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.WaitContainerResultCallback;

public class DockerContainerCreator {

    public static void main(String[] args) {
        // Docker connection configuration
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();

        // Pull the Docker image (optional if image already exists locally)
        String imageName = "ubuntu";
        dockerClient.pullImageCmd(imageName).exec(new PullImageResultCallback()).awaitSuccess();

        // Create container configuration
        CreateContainerResponse container = dockerClient.createContainerCmd(imageName)
                .withCmd("echo", "Hello, Docker!")
                .withExposedPorts(ExposedPort.tcp(80))
                .exec();

        // Start the container
        dockerClient.startContainerCmd(container.getId()).exec();

        // Wait for the container to finish executing
        dockerClient.waitContainerCmd(container.getId()).exec(new WaitContainerResultCallback()).awaitStatusCode();

        // Cleanup: Stop and remove the container
        dockerClient.stopContainerCmd(container.getId()).exec();
        dockerClient.removeContainerCmd(container.getId()).exec();
    }
}

