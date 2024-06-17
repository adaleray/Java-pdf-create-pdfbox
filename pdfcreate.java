import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
//-------------------------------------------------------------------



 private void createPDFReceiptForMonth(String[] yurtBilgi) {
    	
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
        	
            @Override
            protected Void doInBackground() {
            	
                progressBar.setVisible(true);
                String selectedAy = (String) comboBoxAy.getSelectedItem();
                String selectedYil = (String) comboBoxYil.getSelectedItem();
                String fileName = "KarRaporu_" + selectedAy + "_" + selectedYil + ".pdf";

                try (PDDocument document = new PDDocument()) {
                    PDPage page = new PDPage();
                    document.addPage(page);

                    PDImageXObject logoImage = PDImageXObject.createFromFile("src/main/logg.png", document);
                    PDType0Font font = PDType0Font.load(document, new File("src/main/Roboto-Bold.ttf"));

                    PDPageContentStream contentStream = new PDPageContentStream(document, page);
                    // Adjust the Y coordinate to make the logo appear lower on the page
                    contentStream.drawImage(logoImage, 50, 700, logoImage.getWidth() / 4, logoImage.getHeight() / 4);
                    contentStream.setFont(font, 12);
                    contentStream.beginText();
                    contentStream.setLeading(14.5f);
                    contentStream.newLineAtOffset(50, 650);

                    contentStream.showText("KAR MAKBUZU");
                    contentStream.newLine();
                    contentStream.newLine();
                    contentStream.showText("Yurt Adı: " + yurtBilgi[0]);
                    contentStream.newLine();
                    contentStream.showText("Yurt Telefon: " + yurtBilgi[1]);
                    contentStream.newLine();
                    contentStream.showText("Yurt Adres: Bardacık Sokak No: 20 Kızılay/ANKARA");
                    contentStream.newLine();
                    contentStream.newLine();
                    contentStream.showText("Tarih: " + selectedAy + " " + selectedYil);
                    contentStream.newLine();
                    contentStream.newLine();

                    for (int i = 0; i < table.getRowCount(); i++) {
                        for (int j = 0; j < table.getColumnCount(); j++) {
                            contentStream.showText(table.getColumnName(j) + ": " + table.getValueAt(i, j).toString());
                            contentStream.newLine();
                        }
                        contentStream.newLine();
                    }

                    contentStream.endText();
                    contentStream.close();

                    document.save(fileName);
                    JOptionPane.showMessageDialog(null, "PDF başarıyla oluşturuldu: " + fileName);
                    Desktop.getDesktop().open(new File(fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "PDF oluşturulurken bir hata oluştu: " + e.getMessage());
                } finally {
                    progressBar.setVisible(false);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "PDF oluşturulurken bir hata oluştu: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }
