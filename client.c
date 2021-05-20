#include <iostream>
#include <chrono>
#include "agenda.h"

using namespace std;

// Funcao para verificar se a entrada do cliente nao tem campo vazio e nao tem virgulas
bool check_str(char* line, int size) {
    if(strlen(line) == 0){
    	return false;
	}

    for(int i = 0; line[i] != '\0' && i < size; i++) {
        if(line[i] == ','){
            return false;
		}
    }

    return true;
}

// Funcao para criar o contato
void create(CLIENT *clnt, char* name) {
	char** result = NULL;

	char address[50];
	char phone[50];

	strcpy(address, "NULL");
	strcpy(phone, "NULL");

	if(check_str(name, 50) && check_str(address, 50) && check_str(phone, 50)) {
		contact con = {name, address, phone};
		chrono::steady_clock sc;
		auto start = sc.now();
		float time_spent = 0.0;
		while(result == NULL && time_spent < 10.0) {
			result = create_1(&con, clnt);
			auto end = sc.now();
			auto time_span = static_cast<chrono::duration<double>>(end - start);
			time_spent = time_span.count();
		}

		if(result != NULL)
			printf("%s",*result);
		else
			printf("ERRO: TEMPO DE ESPERA DA RESPOSTA DO SERVIDOR ESGOTOU\n");
	} else 
		printf("ERRO: ENTRADA COM CAMPO VAZIO OU PRESENCA DE VIRGULA\n");
}

// Funcao para remover um contato
void remove(CLIENT *clnt, char* name) {
	char** result = NULL;
	
	char address[50];
	char phone[50];

	strcpy(address, "NULL");
	strcpy(phone, "NULL");

	if(check_str(name, 50)) {
		contact con = {name, address, phone};
		chrono::steady_clock sc;
		auto start = sc.now();
		float time_spent = 0.0;
		while(result == NULL && time_spent < 10.0) {
			result = delete_1(&con, clnt);
			auto end = sc.now();
			auto time_span = static_cast<chrono::duration<double>>(end - start);
			time_spent = time_span.count();
		}

		if(result != NULL)
			printf("%s",*result);
		else
			printf("ERRO: TEMPO DE ESPERA DA RESPOSTA DO SERVIDOR ESGOTOU\n");
	} else
		printf("ERRO: ENTRADA COM CAMPO VAZIO OU PRESENCA DE VIRGULA\n");
}

// Funcao para atualizar o contato
void update(CLIENT *clnt, char* name, char* address, char* phone) {
	char** result = NULL;
	// Verifica se a entrada esta valida
	if(check_str(name, 50) && check_str(address, 50)) {
		contact con = {name, address, phone};
		chrono::steady_clock sc;
		auto start = sc.now();
		float time_spent = 0.0;
		while(result == NULL && time_spent < 10.0) {
			result = update_1(&con, clnt);
			auto end = sc.now();
			auto time_span = static_cast<chrono::duration<double>>(end - start);
			time_spent = time_span.count();
		}

		if(result != NULL)
			printf("%s",*result);
		else
			printf("ERRO: TEMPO DE ESPERA DA RESPOSTA DO SERVIDOR ESGOTOU\n");
	} else
		printf("ERRO: ENTRADA COM CAMPO VAZIO OU PRESENCA DE VIRGULA\n");
}


// Funcao para ler um contato
void read(CLIENT *clnt) {
	char** result = NULL;

	char name[50];
	char address[50];
	char phone[50];

	strcpy(name, "Well");
	strcpy(address, "NULL");
	strcpy(phone, "NULL");

	contact con = {name, address, phone};
	chrono::steady_clock sc;
	auto start = sc.now();
	float time_spent = 0.0;
	while(result == NULL && time_spent < 10.0) {
		result = read_1(&con, clnt);
		auto end = sc.now();
		auto time_span = static_cast<chrono::duration<double>>(end - start);
		time_spent = time_span.count();
	}

	if(result != NULL)
		printf("%s",*result);
	else
		printf("ERRO: TEMPO DE ESPERA DA RESPOSTA DO SERVIDOR ESGOTOU");

}

// Funcao para verificar se uma string representa um numero
bool check_number(char* str, int size) {
  	for (int i = 0; i < size && str[i] != '\0'; i++) {
	   if (isdigit(str[i]) == false)
	      return false;
	}
      return true;
}

int main( int argc, char *argv[]) {
   CLIENT *clnt;

   if (argc != 2) {
      fprintf (stderr,"Usage: %s hostname\n",argv[0]);
      exit (1);
   }

   clnt = clnt_create (argv[1], DIARY_PROG, DIARY_VERSION, "udp");

   if (clnt == (CLIENT *) NULL) {
      clnt_pcreateerror (argv[1]);
      exit(2);
   }

   bool continuar = true;
   int result = 0;

   char escolha[50];
   char name[50];
   char address[50];
   char phone[50];

   while(continuar) {
   		printf("Gerenciador de arquivo\n");
		printf("O que deseja fazer?\n");
		printf("1 - Criar arquivo\n");
		printf("2 - Remover arquivo\n");
		printf("3 - Listar arquivo\n");
		printf("4 - Renomear arquivo\n");
		printf("5 - Sair do Programa\n");
		printf("Resposta: ");
		cin.getline(escolha, 50);

		if(!check_number(escolha, sizeof(escolha)))
			result = 10;
		else
			result = stoi(escolha);

		if(result == 1){
			printf("Digite o diretorio/arquivo: ");
			cin.getline(name, 50);
			create(clnt, name);		
		}else if(result == 2){
			printf("Digite o diretorio/arquivo: ");
			cin.getline(name, 50);
			remove(clnt, name);
		}else if(result == 3){
			read(clnt);
		}else if(result == 4){
			printf("Digite o nome do arquivo: ");
			cin.getline(name, 50);
			printf("Digite o novo nome do arquivo: ");
			cin.getline(address, 50);
			update(clnt, name, address, phone);
		}else if(result == 5){
			continuar = false;
		}else{
			printf("ERRO: DIGITE UMA OPCAO VALIDA\n");
		}
   }

   return (0);
} 
