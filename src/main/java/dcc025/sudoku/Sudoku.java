/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package dcc025.sudoku;

import java.util.Scanner;

/**
 *
 * @author gabri
 */

//Nome: Gabriela Singulani Marques
//Matricula: 202265037A
public class Sudoku {
    
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        int option;//armazenará a opção escolhida pelo jogador
        
        do{
            //menu do jogo
            System.out.println("=======Sudoku=======");
            System.out.println("(1)Novo jogo aleatório");
            System.out.println("(2)Definir jogo");
            System.out.println("(3)Sair");
            System.out.print("\nSelecione uma opção: ");
            
            option = teclado.nextInt();
            Tabuleiro tabuleiro = new Tabuleiro(); //cria um novo tabuleiro
            
            switch(option){
                case 1,2 -> tabuleiro.startJogo(option);
                case 3-> System.out.println("Saindo do jogo...");
                default -> System.out.println("Erro: opção inválida!");
            }
        }
        while(option!=3); //sairá do loop caso a opção escolhida seja 3
    }
}
