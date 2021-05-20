#include <iostream>
#include <fstream>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <dirent.h>
#include <vector>
#include "agenda.h"

using namespace std;

//funcao CREATE
char** create_1_svc (contact *con, struct svc_req *rqstp) {
   static char* result = (char*) malloc(sizeof(char) * 100);

    result = strcat(result,"Sucesso ao adicionar diretorio/arquivo\n");
    
    printf("Recebi chamado: CREATE %s\n",con->name);
    
    FILE *arq;

    arq = fopen(con->name,"w");

    fclose(arq);

   return (&result);
}

//funcao READ
char** read_1_svc (contact *con, struct svc_req *rqstp) {
	static char* result = (char*) malloc(sizeof(char) * 200);
	char retorno[] = "";
    
  printf("Recebi chamado: READ %s\n",con->name);

  DIR *dir;
  struct dirent *lsdir;
  
  dir = opendir("/home/wellwlds/gerenciador-de-arquivos/");

  while ( ( lsdir = readdir(dir) ) != NULL ){
     // printf ("%s\n", lsdir->d_name);
      strcat(result,lsdir->d_name);
      strcat(result,"\n");
      result = strcat(result,lsdir->d_name);
  }

  closedir(dir);

  return (&result);
}

//funcao UPDATE
char** update_1_svc (contact *con, struct svc_req *rqstp) {
    static char* result = (char*) malloc(sizeof(char) * 100);
    
    char oldname[] = "/home/wellwlds/gerenciador-de-arquivos/";
    strcat(oldname,con->name);

    char newname[] = "/home/wellwlds/gerenciador-de-arquivos/";
    strcat(newname,con->address);

    rename(oldname,newname);

    return (&result);
}

//funcao DELETE
char** delete_1_svc (contact *con, struct svc_req *rqstp) {
    static char* result = (char*) malloc(sizeof(char) * 100);

    char filename[] = "/home/wellwlds/gerenciador-de-arquivos/";
    strcat(filename,con->name);

    remove(filename);

    return (&result);
}