package pdv;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import pdv.Conexao; 

class ValidadorTeste {

@Test
public void testeCPFValido() {
		assertTrue(Validador.isCPFValido("07855650350"));
	}



@Test
public void testeCPFInvalido() {
	assertFalse(Validador.isCPFValido("05946065156065"),"Erro");
}
public void testNomeInvalido() {
	String nome = "Denis123";
	assertFalse(nome.matches("^[A-Za-z\\s]+$"));
}
public void testeConexaoBanco() {
	Connection conn = Conexao.conectar();
	assertNotNull(conn,"A conexão falhou");
	try {
		assertFalse(conn.isClosed());
		conn.close();
	}catch(SQLException e) {
		fail("Erro ao fechar conexão:"+e.getMessage());
	}
	
}

@Test
public void testeCalculoDesconto() {
	double valorCompra = 100;
	double valorComDesconto = CalculadoraVenda.calcular(valorCompra,5000);
	assertEquals(90.00, valorComDesconto,0.001,"O desconto falhou");
}
}