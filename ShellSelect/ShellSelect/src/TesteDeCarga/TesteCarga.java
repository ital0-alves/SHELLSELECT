package TesteDeCarga;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class TesteCarga {

	@Test
	public void TesteCargaSistema() {
		Clientes dao = new Clientes();
		long tempoInicial = System.currentTimeMillis();
		
		int quantidade = 20000;
		for (int i = 0;i<quantidade;i++) {
			try {
				String cpf = String.format("%011d", i);
				String email = "cliente"+i+"gmail.com";
				dao.salvarCompleto("cliente"+i,cpf,email,"85986076060","60721000","Rua dos bobos"+i,"Centro","Fortaleza","CE",0);
					
		}catch(SQLException e) {
			fail("Falha no teste"+e.getMessage());
		}
	}
		long tempoFinal = System.currentTimeMillis();
		long tempoTotal = tempoFinal - tempoInicial;
		
		System.out.println("Tempo total para "+quantidade+"cadastros completos: "+ tempoTotal + "ms");
		assertTrue(tempoTotal<20000,"banco de dados lento");
	}
}
