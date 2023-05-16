/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dcc025.sudoku;

import java.util.*;

/**
 *
 * @author gabri
 */

//Nome: Gabriela Singulani Marques
//Matricula: 202265037A
public class Tabuleiro {
    private int [][] tab; //matriz do tabuleiro
    private int [][] check; //matriz que armazena as posições de casas fixas
    private int n; //armazena o número de casas preenchidas no tabuleiro (inclusive as fixas)
    
    public Tabuleiro(){
        tab = new int [9][9];
        check = new int [9][9];
        n=0;
        
        //inicializa as matrizes com 0 em todas as casas;
        for(int i=0; i<9; i++)
            for(int j=0; j<9; j++){
                tab[i][j]=0;
                check[i][j]=0;
            }
    }
    
    public void criaAleatorio(int num){
        Random random = new Random();
        int linha, coluna, valor; //variáveis para linha(l), coluna(c) e valor(v)
        int result; //armazena o número de erro que a função addCasa retorna
        
        for(int i=0; i<num; i++){
            //gera um número aleatório para linha, coluna e valor
            linha = random.nextInt(9)+1;
            coluna = random.nextInt(9)+1;
            valor = random.nextInt(9)+1;
            result = addCasa(linha, coluna, valor,true); //a casa adicionada deverá ser fixa
            if(result!=0){
                //repetirá o loop caso a casa aleatorizada não for válida
                i--;
            }
        }
    }
    
    public void criaCustom(){
        Scanner teclado = new Scanner(System.in);
        String input; //armazenará a entrada do jogador
        
        do{
            printTabuleiro();
            System.out.println("\nDigite a(s) posição(ões), seguindo o formato"
                                + " (linha,coluna,valor)\n"
                                + "- 'sair' para começar o jogo");
            input = teclado.nextLine();
            leCasa(input,true);//a casa adicionada deverá ser fixa (true)
        }
        while(!input.equalsIgnoreCase("sair"));//sairá do loop quando o jogador digitar "sair"
    }
    
    private void auxStartJogo(int type){
        Scanner teclado = new Scanner(System.in);
        
        switch(type){
            case 1 -> {
                System.out.println("\nQuantas casas devem ser adicionadas?");
                int i = teclado.nextInt();
                criaAleatorio(i);
            }
            case 2 -> criaCustom();
            default -> System.out.println("\nErro: input inválido\n");
        }
    }
    
    public void startJogo(int type){
        Scanner teclado = new Scanner(System.in);
        String option; //armazenará a entrada do jogador
        
        auxStartJogo(type);
        
        System.out.println("\nO jogo começou!\n");
        do{
            printTabuleiro();
            System.out.println("\nDigite a(s) posição(ões) que deseja completar, "
                                + "seguindo o formato (linha,coluna,valor)\n"
                                + "- 'sair' para desistir\n- 'dica' para receber dica\n- 'remover' para remover uma casa"
                                + "\n- 'verificar' para verificar o tabuleiro");
            option = teclado.nextLine();
            switch(option){
                case "dica" -> dica();
                case "remover" -> removeCasa();
                case "verificar" -> verifica();
                default -> leCasa(option,false);
            }
            if(this.n==81) //verifica se já há 81 casas preenchidas no tabuleiro
                option = checkVitoria(option);
            if(option.equalsIgnoreCase("restart")){
                zeraTabuleiro();
                System.out.println("(1)Novo jogo aleatório");
                System.out.println("(2)Definir jogo");
                System.out.print("\nSelecione uma opção: ");
                int i = teclado.nextInt();
                teclado.nextLine();
                auxStartJogo(i);
                if(i!=1 && i!=2)
                    option = "sair";
            }
        }
        while(!option.equalsIgnoreCase("sair")); //sairá do loop quando a opção for "sair"
        System.out.println("\nVoltando ao menu...\n");
    }
    
    private void dica(){
        int linha = -1, coluna= -1, valor=0;
        outerLoop:
        for(int i=0; i<9; i++)
            for(int j=0; j<9; j++)
                if(tab[i][j]==0){
                    linha = i+1;
                    coluna = j+1;
                    break outerLoop;
                }
        if(linha==-1){
            verifica();
            return;
        }
        for(int i=1; i<=9; i++)
            if(!casaIncorreta(linha, coluna, i)){
                valor = i;
                break;
            }
        if(valor==0){
            System.out.println("\nErro: Não foi possível encontrar casas possíveis\n");
            return;
        }
        System.out.println("\nDica: a casa (" + linha + ", " + coluna + ", " + valor + ") é possível\n");
    }
    
    private void verifica(){
        int linha=0, coluna=0, valor=0;
        outerLoop:
        for(int i=0; i<9; i++)
            for(int j=0; j<9; j++)
                if(casaIncorreta(i+1, j+1, tab[i][j])){
                    linha = i+1;
                    coluna = j+1;
                    valor = tab[i][j];
                    break outerLoop;
                }
        if(linha==0){
            System.out.println("\nNão há casas inválidas no tabuleiro\n");
            return;
        }
        System.out.println("\nAtenção: a casa (" + linha + ", " + coluna + ", " + valor + ") possui conflito com outra casa\n");
    }
    
    private void zeraTabuleiro(){
        n=0;
        
        //preenche as matrizes com 0 em todas as casas;
        for(int i=0; i<9; i++)
            for(int j=0; j<9; j++){
                tab[i][j]=0;
                check[i][j]=0;
            }
    }
    
    private void leCasa(String input, boolean fixa)
    {
        input = input.strip(); //remove espaços em branco da string
            
        int i = input.length();
        if(i%7==0) //o tamanho da string será um multiplo de 7 se estiver dentro do formato especificado
        {
            //substitui os parêntesis com espaço e depois separa cada casa
            input = input.replace("(", " ");
            input = input.replace(")", " ");
            //input = input.replace(")(", " ");
            //(9,9,9)(1,1,1)
            input = input.trim();
            String [] points = input.split(" ");
            
            processaInput(points,fixa);
        }
        else if(!input.equalsIgnoreCase("sair"))//caso o input não tenha sido "sair" é porque ele é inválido
            System.out.println("\nERRO: input inválido, não use espaços e digite no formato (linha,coluna,valor)!\n");
    }
    
    private void tratamentoErro(int erro, int linha, int coluna, int valor, boolean fixa){
        switch(erro)
        {
            case 1 -> System.out.println("\nErro: posição inválida\n");
            case 2 -> System.out.println("\nErro: valor de entrada inválido\n");
            case 3 -> System.out.println("\nErro: casa fixa\n");
            case 4 -> {
                //responsável por avisar o jogador que a casa está incorreta
                System.out.println("\nAviso: posição (" + linha + ", " + coluna + ", " + valor + ") incorreta, deseja manter? (s|n)");
                Scanner teclado = new Scanner(System.in);
                String option = teclado.nextLine();
                
                //caso o jogador escolha manter a casa incorreta, ela será adicionada
                if(option.equalsIgnoreCase("s"))
                    auxAddCasa(linha, coluna, valor, fixa);
            }
        }
    }
    
    private void processaInput(String [] points, boolean fixa){
        int erro; //armazenará o id do erro retornado pela função addCasa
        for(int j=0; j<points.length; j++){
            if(!points[j].isEmpty()){
                String [] coordenadas = points[j].split(",");
                
                int linha, coluna, valor; //variáveis para linha(lin), coluna(col) e valor(val)
                linha = Integer.parseInt(coordenadas[0]);
                coluna = Integer.parseInt(coordenadas[1]);
                valor = Integer.parseInt(coordenadas[2]);
                
                erro = addCasa(linha, coluna, valor, fixa);
                tratamentoErro(erro, linha, coluna, valor, fixa);
            }
        }
    }
    
    private String checkVitoria(String option){
        int nInc = 0; //variável que armazena o número de casas incorretas no tabuleiro
        //conta o número de casas incorretas no tabuleiro
        for(int i=0; i<9; i++)
            for(int j=0; j<9; j++)
                if(casaIncorreta(i+1,j+1,tab[i][j]))
                    nInc++;
        
        if(nInc==0){
            Scanner teclado = new Scanner(System.in);
            
            System.out.println("\nParabéns! Você venceu o jogo! Ebaaaaa\n");
            
            //oferece a opção de jogar outro jogo
            System.out.println("Deseja jogar novamente? (s|n)");
            String input = teclado.nextLine();
            
            if(input.equalsIgnoreCase("s"))
                return "restart";
            else if(!input.equalsIgnoreCase("n"))
                System.out.println("\nErro: input inválido\n");
            return "sair"; //retorna "sair" para voltar ao menu
        }
        
        //caso o número de casas incorretas seja diferente de 0, o jogo não irá terminar
        System.out.println("\nErro: há " + nInc + " casas inválidas em seu tabuleiro!\n");
        verifica();
        return option;
    }
    
    private void auxAddCasa(int linha, int coluna, int valor, boolean fixa){
        if(tab[linha-1][coluna-1]==0) //verifica se a casa não estava preenchida antes
            this.n++;
        if(fixa) //verifica se a casa que está sendo adicionada deve ser fixa
            check[linha-1][coluna-1]=1;
        if(valor==0) //verifica se o valor é 0, ou seja, se o jogador está removendo a casa
            this.n--;
        tab[linha-1][coluna-1]=valor; //adiciona a casa
    }
    
    private int addCasa(int linha, int coluna, int valor, boolean fixa){
        if((linha<=0 || linha>9) || (coluna<=0 || coluna>9)) //verifica se a linha e a coluna são válidos
            return 1;
        if(valor<0 || valor>9) //verifica se o valor é válido
            return 2;
        if(casaFixa(linha, coluna))
            return 3;
        if(casaIncorreta(linha, coluna, valor))
            return 4;
        
        //casa só é adicionada se a posição passar por todos os testes anteriores
        auxAddCasa(linha, coluna, valor, fixa);
        return 0;
    }
    
    private void removeCasa(){
        Scanner teclado = new Scanner(System.in);
        System.out.println("\nDigite a casa que deseja remover no formato (linha,coluna)");
        String input = teclado.nextLine();
        input = input.strip();
        if(input.length()!=5){
            System.out.println("\nErro: input inválido\n");
            return;
        }
        input = input.replace("(", " ");
        input = input.replace(")", " ");
        input = input.trim();
        String [] coordenadas = input.split(",");
        int linha = Integer.parseInt(coordenadas[0]);
        int coluna = Integer.parseInt(coordenadas[1]);
        int error = addCasa(linha, coluna, 0, false);
        tratamentoErro(error, linha, coluna, 0, false);
    }
    
    private boolean casaFixa(int linha, int coluna){
        if(n==0)//caso não haja nenhuma casa preenchida no tabuleiro
            return false;
        return check[linha-1][coluna-1]!=0;//retorna true se a casa é fixa
    }
    
    private boolean casaIncorreta(int linha, int coluna, int valor){
        if(valor==0)//se o valor a ser adicionado é 0 (vazio), a casa não é incorreta (false)
            return false;
        int i;
        int j;
        boolean result=false;//variável que armazenará o resultado da testagem
        
        //atualiza valores da linha e da coluna para começarem pelo índice 0 ao invés de 1
        linha = linha-1;
        coluna = coluna-1;
        
        //verifica se já tem o valor na coluna da posição
        j=coluna;
        for(i=0; i<9; i++)
            result = result || (tab[i][j]==valor && i!=linha);
        
        //verifica se já tem o valor na linha da posição
        i=linha;
        for(j=0; j<9; j++)
            result = result || (tab[i][j]==valor && j!=coluna);
        
        //verifica se já tem o valor no bloco 3x3 da posição
        for(i = (linha/3)*3; i<3*(linha/3)+3; i++)
            for(j = (coluna/3)*3; j<3*(coluna/3)+3;j++)
                result = result || (tab[i][j]==valor && i!=linha && j!=coluna);
        
        return result;//retornará true se a casa for incorreta
    }
    
    public void printTabuleiro(){
        System.out.println("======Tabuleiro=====");
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                System.out.print(tab[i][j] + " ");
                
                if((j+1)%3==0&& (j+1)!=9)
                    System.out.print("|");//separa cada bloco 3x3 a cada 3 colunas
            }
            System.out.println();
            
            if((i+1)%3==0 && (i+1)!=9){
                for(int k=0; k<20; k++)
                    System.out.print("-");//separa cada bloco 3x3 a cada 3 linhas
                System.out.println();
            }
        }
    }
    
}
