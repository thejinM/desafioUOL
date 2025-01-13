package src.teste;

public class carrinhoException extends RuntimeException 
{
    public carrinhoException(String mensagem) 
    {
        super(mensagem);
    }
}