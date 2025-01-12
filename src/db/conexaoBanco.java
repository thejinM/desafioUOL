package src.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
  
  public class conexaoBanco
  {
      private static final String URL = "jdbc:mysql://localhost:3306/desafioUOL?useSSL=false&serverTimezone=UTC";
      private static final String USUARIO = "root";
      private static final String SENHA = "postgresjm.49";
      private static Connection conexao;
  
      public static Connection getConexao()
      {
          if (conexao == null)
          {
              try
              {
                  Class.forName("com.mysql.cj.jdbc.Driver");
                  conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
              } 
              catch (ClassNotFoundException e)
              {
                  throw new RuntimeException("Driver JDBC não encontrado.", e);
              } 
              catch (SQLException e)
              {
                  throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
              }
          }
          return conexao;
      }
  
      public static void fecharConexao()
      {
          if (conexao != null)
          {
              try
              {
                  conexao.close();
                  conexao = null;
              } 
              catch (SQLException e)
              {
                  throw new RuntimeException("Erro ao fechar a conexão com o banco de dados: " + e.getMessage(), e);
              }
          }
      }
  
      public static boolean isConexaoAtiva()
      {
          try
          {
              return conexao != null && !conexao.isClosed();
          } 
          catch (SQLException e)
          {
              throw new RuntimeException("Erro ao verificar o estado da conexão: " + e.getMessage(), e);
          }
      }
  
      public static void reiniciarConexao()
      {
          fecharConexao();
          getConexao();
      }
  }  