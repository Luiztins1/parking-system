package com.github.Luiztins1.parking_system.utils;

import de.vwsoft.barcodelib4j.image.BarExporter;
import de.vwsoft.barcodelib4j.image.CompoundColor;
import de.vwsoft.barcodelib4j.oned.Barcode;
import de.vwsoft.barcodelib4j.oned.BarcodeType;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class BarcodeGeneratorUtil {

    public String generateTicketBarcode(String ticketNumber) {
        try{
            Barcode barcode = Barcode.newInstance(BarcodeType.CODE128);
            barcode.setContent(ticketNumber, false, false);

            //TODO: set font.
            barcode.setFont(new Font("OCR-B", Font.PLAIN, 1));
            barcode.setFontSizeAdjusted(true);
            barcode.setTextOffset(-0.3);

            //TODO: set size.
            final double widthMM = 50.0, heightMM = 30.0;

            BarExporter exporter = new BarExporter(widthMM, heightMM);
            exporter.setTitle("Code 128: " + barcode.getText());
            exporter.setForeground(new CompoundColor(Color.RED));
            exporter.setBackground(new CompoundColor(Color.YELLOW));

            //TODO: set graphics2d.
            Graphics2D g2d = exporter.getGraphics2D();
            barcode.draw(g2d, 0.0, 0.0, widthMM, heightMM);
            g2d.dispose();

            //TODO: export barcode in svg with in byte array.
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                exporter.writeSVG(baos);
                return baos.toString(StandardCharsets.UTF_8);
            }

        }catch(Exception e){
            throw new RuntimeException("Erro ao gerar o código de barras do ticket", e);
        }
    }
}
