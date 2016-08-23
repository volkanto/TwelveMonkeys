package com.twelvemonkeys.imageio.plugins.jpeg;

import com.twelvemonkeys.imageio.metadata.jpeg.JPEG;
import com.twelvemonkeys.lang.Validate;

import java.io.DataInput;
import java.io.IOException;

/**
 * Segment.
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: harald.kuhr$
 * @version $Id: Segment.java,v 1.0 22/08/16 harald.kuhr Exp$
 */
abstract class Segment {
    final int marker;

    protected Segment(final int marker) {
        this.marker = Validate.isTrue(marker >> 8 == 0xFF, marker, "Unknown JPEG marker: 0x%04x");
    }

    public static Segment read(int marker, String identifier, int length, DataInput data) throws IOException {
        // TODO: Fix length inconsistencies...
//        System.err.print("marker: " + marker);
//        System.err.println(" length: " + length);
        switch (marker) {
            case JPEG.DHT:
                return HuffmanTable.read(data, length);
            case JPEG.DQT:
                return QuantizationTable.read(data, length - 2);
            case JPEG.SOF0:
            case JPEG.SOF1:
            case JPEG.SOF2:
            case JPEG.SOF3:
            case JPEG.SOF5:
            case JPEG.SOF6:
            case JPEG.SOF7:
            case JPEG.SOF9:
            case JPEG.SOF10:
            case JPEG.SOF11:
            case JPEG.SOF13:
            case JPEG.SOF14:
            case JPEG.SOF15:
                return Frame.read(marker, data, length);
            case JPEG.SOS:
                return Scan.read(data, length);
            case JPEG.COM:
                return Comment.read(data, length);
            case JPEG.APP0:
            case JPEG.APP1:
            case JPEG.APP2:
            case JPEG.APP3:
            case JPEG.APP4:
            case JPEG.APP5:
            case JPEG.APP6:
            case JPEG.APP7:
            case JPEG.APP8:
            case JPEG.APP9:
            case JPEG.APP10:
            case JPEG.APP11:
            case JPEG.APP12:
            case JPEG.APP13:
            case JPEG.APP14:
            case JPEG.APP15:
                return AppSegment.read(marker, identifier, data, length);
            // TODO: JPEG.DRI?
            default:
                return Unknown.read(marker, length, data);
        }
    }
}
