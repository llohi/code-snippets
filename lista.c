#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>

/* Example implementation of a linked list */

typedef struct Solmu
{
  void *data;
  struct Solmu *seur;
} Solmu;

Solmu * s_luo(const void * data, size_t dataKoko, Solmu *seur);

typedef struct
{
  size_t n;
  Solmu *paa;
  void (*tulostaArvo)(FILE *virta, const void *arvo);
  size_t dataKoko;

} Lista;

Solmu * s_luo(const void * data, size_t dataKoko, Solmu *seur)
{
  Solmu *ds = malloc(sizeof(Solmu));
  ds->data = (void *) malloc(dataKoko);
  memcpy(ds->data, data, dataKoko);
  ds->seur = seur;
  return ds;
}

Lista * ll_luo(size_t dataKoko, void (*tulostaArvo)(FILE *virta, const void *arvo))
{
  Lista *dl = malloc(sizeof(Lista));
  dl->n = 0;
  dl->paa = NULL;
  dl->tulostaArvo = tulostaArvo;
  dl->dataKoko = dataKoko;
  return dl;
}

void ll_tuhoa(Lista *dl)
{
  while(dl->n > 0)
  {
    ll_poistaEdesta(dl);
  }
  free(dl);
}

Solmu * ll_lisaaEteen(Lista *dl, const void *arvo)
{
  dl->paa = s_luo(arvo, dl->dataKoko, dl->paa);
  dl->n += 1;
  return dl->paa;
}

void ll_poistaEdesta(Lista *dl)
{
  if(dl->n > 0)
  {
    Solmu *vanhaPaa = dl->paa;
    free(dl->paa->data);
    dl->paa = dl->paa->seur;
    free(vanhaPaa);
    dl->n -= 1;
  }
}

void ll_tulosta(const Lista *lista, FILE *virta)
{
  Solmu *s;
  if (lista->tulostaArvo == NULL) return;
  printf("Lista:");
  for(s = lista->paa; s != NULL; s = s->seur)
  {
    printf(" ");
    lista->tulostaArvo(virta, s->data);
  }
  printf("\n");
}

Solmu * ll_moniLisaa(Lista *lista, size_t i, const void *arvo, ...)
{
  Solmu *e, *s, *u, *f;  /*  return f  */
  size_t j;
  va_list pl;

  /*  loop to correct index
        ptr e is either last element or
        the one previous to new element  */
  e = lista->paa;
  if (i > 0 && lista->n > 0) {
    if (i >= lista->n) {
      while (e->seur != NULL) {
          e = e->seur;
      }

    } else {
      e = lista->paa;
      for (j = 0; j < i - 1; j++) {
          e = e->seur;
      }
    }
  }

  va_start(pl, arvo);
  /*  take care of first value seperately to store it in f  */
  if (i == 0 || lista->n == 0) {
    u = ll_lisaaEteen(lista, arvo);
    arvo = va_arg(pl, void *);
    e = u;
    f = u;
    u = NULL;

  } else {
    s = e->seur;
    /*  add new value  */
    u = (Solmu *) malloc(sizeof(Solmu));
    u->data = (void *) malloc(lista->dataKoko);
    memcpy(u->data, arvo, lista->dataKoko);
    e->seur = u;
    u->seur = s;
    lista->n++;
    /*  move ptrs  */
    e = u;
    f = u;  /*  return value  */
    s = e->seur;
    arvo = va_arg(pl, void *);
    u = NULL;
  }
  s = e->seur;

  /*  add rest of values  */
  while (arvo != NULL)
  {
    u = (Solmu *) malloc(sizeof(Solmu));
    u->data = (void *) malloc(lista->dataKoko);
    memcpy(u->data, arvo, lista->dataKoko);
    e->seur = u;
    u->seur = s;
    lista->n++;

    e = u;
    s = e->seur;
    arvo = va_arg(pl, void *);
    u = NULL;
  }
  va_end(pl);
  return f;
}
