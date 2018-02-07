package project.system;

import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.List;

public class ClipboardFile implements Transferable, ClipboardOwner {
    public List files;

    public ClipboardFile(List files) {
        this.files = files;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { DataFlavor.javaFileListFlavor };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == DataFlavor.javaFileListFlavor;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return this.files;
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }
}
