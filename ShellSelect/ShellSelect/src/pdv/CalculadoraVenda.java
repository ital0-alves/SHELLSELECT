
package pdv;

public class CalculadoraVenda {

    
    public static double calcular(double precoBase, int pontosCliente) {
        double percentualDesconto = 0.0;

        if (pontosCliente >= 5000) {
            percentualDesconto = 0.10; 
        } else if (pontosCliente > 0) {
            percentualDesconto = 0.05; 
        }

        return precoBase - (precoBase * percentualDesconto);
    }
}