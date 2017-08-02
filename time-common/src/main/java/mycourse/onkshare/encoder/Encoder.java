package mycourse.onkshare.encoder;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

public abstract class Encoder {

    public abstract boolean getResult(File target1, File target2);

    public abstract String getMethodName();

    public abstract String getFileIdentify(File file);

    public abstract String getFileIdentify(FileInputStream fis);

    public abstract String getFileIdentify(ByteBuffer byteBuffer);
}
