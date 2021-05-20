/* addsub.x : definicao da interface */

/*#define PROGRAM_NUMBER 12345678
#define VERSION_NUMBER 1*/

#define PROGRAM_NUMBER 1111111
#define VERSION_NUMBER 1

/* estrutura de dados */
struct contact {
    string name<50>;
    string address<50>;
    string phone<50>;
};

/* interface grafica para o client */
program DIARY_PROG {
   version DIARY_VERSION{
     string CREATE (contact) = 1;
     string READ (contact) = 2;
     string UPDATE (contact) = 3;
     string DELETE (contact) = 4;
   }
   = VERSION_NUMBER;
}
= PROGRAM_NUMBER;
