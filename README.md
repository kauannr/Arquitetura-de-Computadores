# Simulador ULA e caminho de dados da Mic-1 


# Sobre o projeto

O objetivo desta atividade é a simulação de uma ULA e a implementação do caminho de dados da Mic-1 a partir dela. 
Criei e modifiquei a ULA para operar com uma palavra de 8 bits como os
seus sinais de controle, sendo a forma da palavra dada por:
SLL8 SRA1 F0 F1 ENA ENB INVA INC
X0 X1 X2 X3 X4 X5 X6 X7
Após isso, implementei o  conjunto de dez variáveis, correspondentes aos registradores da Mic-1:
- Os registradores de 32 bits: H, OPC, TOS, CPP, LV, SP, PC, MDR e MAR.
- O registrador de 8 bits MBR.
Em seguida, implementei as funções lógicas.

Na última etapa do projeto, a ULA foi modificada para receber palavras de 21 bits com o seguinte arranjo:
- Controle da ULA: 8 bits
- Controle do barramento C: 9 bits
- Controle do barramento B: 4 bits
  
Por exemplo, a instrução 001101001010000000000
Os últimos 4 bits definem que o registrador MDR está comandando o barramento B. Logo, a entrada B da ULA será B = MDR. Os 8 primeiros bits definem que a ULA executará uma operação do tipo Sd = B. Como B = MDR, tem-se que Sd = MDR. E por fim, os 9 bits do meio definem que os registradores H e TOS estão habilitados para escrita, de modo que o valor da ULA passará para eles, ou seja, ao final da operação tem-se que H = MDR e TOS = MDR.

# Tecnologias utilizadas
## Back end
- Java
# Como executar o projeto

Pré-requisitos: Java 11

```bash
# clonar repositório
git clone https://github.com/kauannr/Springboot3-JPA

# entrar na pasta do projeto 
cd "pasta do projeto"

# passar as instruções no arquivo instrucoes.txt

# modificar, caso queira, os valores dos registradores dentro do código

# executar o projeto e visualizar a saída no arquivo log.txt 


```

# Autor

Kauan Ferreira Rodrigues

https://www.linkedin.com/in/kauan-ferreira-922671240/
