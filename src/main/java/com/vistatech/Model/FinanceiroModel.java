// src/main/java/com/vistatech/model/FinanceiroModel.java
package com.vistatech.Model;

import java.util.ArrayList;
import java.util.List;

public class FinanceiroModel {
    private List<Object[]> movimentacoes;

    public FinanceiroModel() {
        this.movimentacoes = new ArrayList<>();
        carregarDadosMockados();
    }

    private void carregarDadosMockados() {
        // Adiciona os 50 registros mockados
        List<Object[]> movimentacoes = new ArrayList<>();

// Entradas (valores positivos)
        movimentacoes.add(new Object[]{1, "Salário", 5000.00, "01/05/2023", "Entrada", "Pagamento mensal", "RH"});
        movimentacoes.add(new Object[]{2, "Aluguel", -2200.00, "01/05/2023", "Saída", "Aluguel matriz", "Financeiro"});
        movimentacoes.add(new Object[]{3, "Folha Pagamento", -18500.00, "02/05/2023", "Saída", "Salários equipe", "RH"});
        movimentacoes.add(new Object[]{4, "Conta de Luz", -480.50, "03/05/2023", "Saída", "Energia matriz", "Financeiro"});
        movimentacoes.add(new Object[]{5, "Internet", -199.90, "04/05/2023", "Saída", "Internet corporativa", "TI"});
        movimentacoes.add(new Object[]{6, "Material Escritório", -320.75, "05/05/2023", "Saída", "Reposição estoque", "Compras"});
        movimentacoes.add(new Object[]{7, "Marketing Digital", -850.00, "06/05/2023", "Saída", "Campanha Google Ads", "Marketing"});
        movimentacoes.add(new Object[]{8, "Manutenção", -670.40, "07/05/2023", "Saída", "Reparos infraestrutura", "Manutenção"});
        movimentacoes.add(new Object[]{9, "Seguros", -420.00, "08/05/2023", "Saída", "Seguro predial", "Financeiro"});
        movimentacoes.add(new Object[]{10, "Software", -299.90, "09/05/2023", "Saída", "Licenças mensais", "TI"});
        movimentacoes.add(new Object[]{12, "Freelance Design", 1200.00, "02/05/2023", "Entrada", "Projeto Site ABC", "Freelancer"});
        movimentacoes.add(new Object[]{11, "Viagem", -1250.00, "10/05/2023", "Saída", "Viagem executiva", "Diretoria"});
        movimentacoes.add(new Object[]{13, "Venda Produto A", 350.50, "03/05/2023", "Entrada", "Venda online", "Vendedor"});
        movimentacoes.add(new Object[]{14, "Aluguel Sala", 1800.00, "05/05/2023", "Entrada", "Aluguel mensal", "Financeiro"});
        movimentacoes.add(new Object[]{15, "Consultoria", 2200.00, "06/05/2023", "Entrada", "Consultoria técnica", "Consultor"});
        movimentacoes.add(new Object[]{16, "Reembolso", 150.75, "07/05/2023", "Entrada", "Reembolso despesas", "Funcionário"});
        movimentacoes.add(new Object[]{17, "Juros Investimento", 320.40, "08/05/2023", "Entrada", "Rendimento CDI", "Investimentos"});
        movimentacoes.add(new Object[]{18, "Venda Produto B", 420.00, "09/05/2023", "Entrada", "Venda balcão", "Vendedor"});
        movimentacoes.add(new Object[]{19, "Serviço Técnico", 580.00, "10/05/2023", "Entrada", "Manutenção equipamentos", "Técnico"});
        movimentacoes.add(new Object[]{20, "Royalties", 750.30, "11/05/2023", "Entrada", "Pagamento de royalties", "Parceiro"});
        movimentacoes.add(new Object[]{21, "Curso Online", 980.00, "12/05/2023", "Entrada", "Venda curso premium", "Educação"});
        movimentacoes.add(new Object[]{22, "Assinaturas", 420.00, "13/05/2023", "Entrada", "Assinaturas mensais", "Marketing"});
        movimentacoes.add(new Object[]{23, "Patrocínio", 1500.00, "14/05/2023", "Entrada", "Patrocínio evento", "Parcerias"});
        movimentacoes.add(new Object[]{24, "Venda Equipamento", 1200.00, "15/05/2023", "Entrada", "Venda usados", "TI"});
        movimentacoes.add(new Object[]{25, "Serviço Terceirizado", 850.00, "16/05/2023", "Entrada", "Limpeza terceirizada", "Limpeza"});
        movimentacoes.add(new Object[]{26, "Impostos", -3200.00, "12/05/2023", "Saída", "DARF mensal", "Contabilidade"});
        movimentacoes.add(new Object[]{27, "Benefícios", -980.00, "13/05/2023", "Saída", "Vale alimentação", "RH"});
        movimentacoes.add(new Object[]{28, "Telefonia", -450.00, "14/05/2023", "Saída", "Planos corporativos", "TI"});
        movimentacoes.add(new Object[]{29, "Treinamento", -1200.00, "15/05/2023", "Saída", "Capacitação equipe", "RH"});
        movimentacoes.add(new Object[]{30, "Eventos", -750.00, "16/05/2023", "Saída", "Coffee break reunião", "Administrativo"});
        movimentacoes.add(new Object[]{31, "Venda E-commerce", 620.00, "17/05/2023", "Entrada", "Pedido #3052", "E-commerce"});
        movimentacoes.add(new Object[]{32, "Frete", -85.00, "17/05/2023", "Saída", "Envio produtos", "Logística"});
        movimentacoes.add(new Object[]{33, "Taxa Cartão", -32.40, "18/05/2023", "Saída", "Taxa operadora", "Financeiro"});
        movimentacoes.add(new Object[]{34, "Devolução", -120.00, "18/05/2023", "Saída", "Devolução cliente", "E-commerce"});
        movimentacoes.add(new Object[]{35, "Venda App", 290.00, "19/05/2023", "Entrada", "Venda mobile", "Mobile"});
        movimentacoes.add(new Object[]{36, "Manutenção Veicular", -380.00, "19/05/2023", "Saída", "Revisão frota", "Frota"});
        movimentacoes.add(new Object[]{37, "Consultoria Jurídica", -650.00, "20/05/2023", "Saída", "Horas advocacia", "Jurídico"});
        movimentacoes.add(new Object[]{38, "Venda Consultoria", 1800.00, "20/05/2023", "Entrada", "Pacote premium", "Consultoria"});
        movimentacoes.add(new Object[]{39, "Material Limpeza", -95.00, "21/05/2023", "Saída", "Reposição produtos", "Limpeza"});
        movimentacoes.add(new Object[]{40, "Hospedagem Site", -89.90, "21/05/2023", "Saída", "Hosting mensal", "TI"});
        movimentacoes.add(new Object[]{41, "Venda E-book", 45.00, "22/05/2023", "Entrada", "Venda digital", "Marketing"});
        movimentacoes.add(new Object[]{42, "Uniforme", -320.00, "22/05/2023", "Saída", "Novos uniformes", "RH"});
        movimentacoes.add(new Object[]{43, "Serviço Gráfico", -150.00, "23/05/2023", "Saída", "Impressão materiais", "Marketing"});
        movimentacoes.add(new Object[]{44, "Venda Assinatura", 199.90, "23/05/2023", "Entrada", "Assinatura anual", "Vendas"});
        movimentacoes.add(new Object[]{45, "Estacionamento", -28.00, "24/05/2023", "Saída", "Diárias mensais", "Frota"});
        movimentacoes.add(new Object[]{46, "Reembolso Cliente", 75.00, "24/05/2023", "Entrada", "Devolução taxa", "Atendimento"});
        movimentacoes.add(new Object[]{47, "Material Construção", -420.00, "25/05/2023", "Saída", "Reforma sala", "Manutenção"});
        movimentacoes.add(new Object[]{48, "Venda Seminário", 1500.00, "25/05/2023", "Entrada", "Ingressos evento", "Eventos"});
        movimentacoes.add(new Object[]{49, "Taxa Bancária", -35.00, "26/05/2023", "Saída", "Tarifa mensal", "Financeiro"});
        movimentacoes.add(new Object[]{50, "Doação", 500.00, "26/05/2023", "Entrada", "Doação parceiro", "Parcerias"});

    }


    // Métodos para acesso aos dados
    public List<Object[]> getTodasMovimentacoes() {
        return new ArrayList<>(movimentacoes); // Retorna cópia para evitar modificações externas
    }

    public List<Object[]> getEntradas() {
        List<Object[]> entradas = new ArrayList<>();
        for (Object[] mov : movimentacoes) {
            if ((Double) mov[2] > 0) {
                entradas.add(mov);
            }
        }
        return entradas;
    }

    public List<Object[]> getSaidas() {
        List<Object[]> saidas = new ArrayList<>();
        for (Object[] mov : movimentacoes) {
            if ((Double) mov[2] < 0) {
                saidas.add(mov);
            }
        }
        return saidas;
    }
}