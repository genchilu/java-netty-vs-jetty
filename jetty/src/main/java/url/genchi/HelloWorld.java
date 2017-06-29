package url.genchi;

import java.util.UUID;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.math3.random.RandomDataGenerator;

@Path("/hello")
public class HelloWorld {
  @GET
  @Path("test")
  @Produces(MediaType.TEXT_PLAIN)
  public String test() {
    for(int i = 0;i<100;i++) {
      UUID uuid = UUID.randomUUID();
    }
    //System.out.println("test");
    return "Test";
  }
}