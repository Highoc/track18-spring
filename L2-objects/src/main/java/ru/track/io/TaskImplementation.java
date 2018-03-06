package ru.track.io;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.track.io.vendor.Bootstrapper;
import ru.track.io.vendor.FileEncoder;
import ru.track.io.vendor.ReferenceTaskImplementation;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Base64;

public final class TaskImplementation implements FileEncoder {

    /**
     * @param finPath  where to read binary data from
     * @param foutPath where to write encoded data. if null, please create and use temporary file.
     * @return file to read encoded data from
     * @throws IOException is case of input/output errors
     */
    @NotNull
    public File encodeFile(@NotNull String finPath, @Nullable String foutPath) throws IOException {
        /* XXX: https://docs.oracle.com/javase/8/docs/api/java/io/File.html#deleteOnExit-- */

        final File fin = new File(finPath);
        final File fout;

        if (foutPath != null) {
            fout = new File(foutPath);
        } else {
            fout = File.createTempFile("based_file_", ".txt");
            fout.deleteOnExit();
        }

        try (
            final InputStream is = new FileInputStream(fin);
            final OutputStream os = new BufferedOutputStream(new FileOutputStream(fout));
        ) {
            //Должно быть кратно 3
            final int bufferSize = 1023;

            byte[] bytesIn = new byte[bufferSize];
            byte[] bytesOut = new byte[bufferSize/3*4];

            int readBytes = 0, addBytes = 0;
            while((readBytes = is.read(bytesIn, 0, bufferSize)) != -1) {
                //Граничный случай, когда надо дополнить нулевыми байтами (до количества, кратного 3)
                addBytes = (3 - readBytes%3)%3;
                for (int i = 0; i < addBytes; i++) {
                    bytesIn[readBytes++] = 0x00;
                }

                for (int i = 0; i < readBytes/3; i++) {
                    bytesOut[4*i]     = (byte) toBase64[(bytesIn[3*i] & 0xFC) >> 2];
                    bytesOut[4*i + 1] = (byte) toBase64[((bytesIn[3*i] & 0x03) << 4) | ((bytesIn[3*i + 1] & 0xF0) >>> 4 )];
                    bytesOut[4*i + 2] = (byte) toBase64[((bytesIn[3*i + 1] & 0x0F) << 2) | ((bytesIn[3*i + 2] & 0xC0) >>> 6 )];
                    bytesOut[4*i + 3] = (byte) toBase64[(bytesIn[3*i + 2] & 0x3F)];
                }

                if (addBytes == 2)
                {
                    bytesOut[readBytes/3*4 - 1] = (byte) '=';
                    bytesOut[readBytes/3*4 - 2] = (byte) '=';
                } else if (addBytes == 1)
                {
                    bytesOut[readBytes/3*4 - 1] = (byte) '=';
                }

                os.write(bytesOut, 0, readBytes/3*4);
            }
        }

        return fout;
    }

    private static final char[] toBase64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    public static void main(String[] args) throws Exception {
        // final FileEncoder encoder = new ReferenceTaskImplementation();
        final FileEncoder encoder = new TaskImplementation();
        // NOTE: open http://localhost:9000/ in your web browser
        (new Bootstrapper(args, encoder))
                .bootstrap("", new InetSocketAddress("127.0.0.1", 9000));
    }

}
