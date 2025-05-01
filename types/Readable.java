package types;

import java.io.IOException;

public interface Readable {

    String getInput() throws IOException;

    String getInputNonBlocking() throws IOException;

}
