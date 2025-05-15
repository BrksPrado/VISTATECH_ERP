package com.vistatech.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.vistatech.GettersSetters.ProdutoEstoque;
import com.vistatech.Model.CadProdutoModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RelatorioService {

    private CadProdutoModel cadProdutoModel;

    public RelatorioService(CadProdutoModel cadProdutoModel) {
        this.cadProdutoModel = cadProdutoModel;
    }

    public void gerarRelatorioProdutosPDF() {
        List<ProdutoEstoque> produtoEstoques = cadProdutoModel.carregarProdutos();
        BaseColor azulVistaTech = new BaseColor(59, 89, 182);
        if (produtoEstoques.isEmpty()) {
            System.out.println("Nenhum produto encontrado para gerar o relatório.");
            return;
        }

        // Cria o documento PDF
        Document document = new Document();
        try {
            // Define o arquivo de saída
            PdfWriter.getInstance(document, new FileOutputStream("relatorio_estoque.pdf"));
            document.open();

            // Configurações de estilo
            Font fonteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, azulVistaTech);
            Font fonteCabecalho = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font fonteDados = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Cria uma tabela para o cabeçalho (logo + título)
            PdfPTable tabelaCabecalho = new PdfPTable(1);
            tabelaCabecalho.setWidthPercentage(100);

            Paragraph cabecalho = new Paragraph();
            cabecalho.setAlignment(Element.ALIGN_CENTER);

            // Adiciona a logo ao parágrafo
            Image logo = Image.getInstance("src/main/resources/logotipo.png");
            logo.scaleToFit(150, 150);
            cabecalho.add(new Chunk(logo, 0, -25));

            // Adiciona um espaço entre a logo e o título
            cabecalho.add(new Chunk("  "));

            cabecalho.add(new Chunk("Relatório do Estoque\n\n", fonteTitulo));

            // Adiciona o parágrafo à célula da tabela
            PdfPCell cellCabecalho = new PdfPCell(cabecalho);
            cellCabecalho.setBorder(Rectangle.NO_BORDER);
            cellCabecalho.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaCabecalho.addCell(cellCabecalho);

            document.add(tabelaCabecalho);

            // Cria uma tabela para os dados
            PdfPTable table = new PdfPTable(6); // 6 colunas: ID, Nome, Preço Custo, Preço Venda, Quantidade, Tipo
            table.setWidthPercentage(100);

            // Cabeçalho da tabela
            String[] cabecalhos = {"ID", "Nome", "Preço de Custo", "Preço de Venda", "Quantidade", "Tipo"};
            for (String cabecalhoColuna : cabecalhos) {
                PdfPCell cell = new PdfPCell(new Paragraph(cabecalhoColuna, fonteCabecalho));
                cell.setBackgroundColor(azulVistaTech);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Preenche a tabela com os dados dos produtos
            for (ProdutoEstoque produtoEstoque : produtoEstoques) {
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(produtoEstoque.getId()), fonteDados)));
                table.addCell(new PdfPCell(new Paragraph(produtoEstoque.getNome(), fonteDados)));
                table.addCell(new PdfPCell(new Paragraph(String.format("R$ %.2f", produtoEstoque.getPrecoCusto()), fonteDados)));
                table.addCell(new PdfPCell(new Paragraph(String.format("R$ %.2f", produtoEstoque.getPrecoVenda()), fonteDados)));
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(produtoEstoque.getQuantidade()), fonteDados)));
                table.addCell(new PdfPCell(new Paragraph(produtoEstoque.getTipo(), fonteDados))); // Adiciona o campo tipo
            }

            // Adiciona a tabela ao documento
            document.add(table);

            System.out.println("Relatório PDF gerado com sucesso: relatorio_estoque.pdf");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao gerar o relatório PDF.");
        } finally {
            // Fecha o documento
            if (document.isOpen()) {
                document.close();
            }
        }
    }
}