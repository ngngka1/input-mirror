package types;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class CustomBufferedReader extends java.io.BufferedReader implements Readable {

    public CustomBufferedReader(Reader r) {
        super(r);
    }

    @Override
    public String getInput() throws IOException {
        return super.readLine();
    }

    @Override
    public String getInputNonBlocking() throws IOException {
        if (!super.ready()) {
            return "";
        }
        return getInput();
    }
}
