This is a decision engine to determine whatever loan should be approved and on what max amount given personal code, 
loan amount and loan period. 

It assumes for now that there are 4 different scenarious only (debt or one of the segments):
1) 49002010965 - debt
2) 49002010976 - segment 1 (credit_modifier = 100)
3) 49002010987 - segment 2 (credit_modifier = 300)
4) 49002010998 - segment 3 (credit_modifier = 1000)

It has the following constraints:
1) Minimum input and output sum can be 2000 €
2) Maximum input and output sum can be 10000 €
3) Minimum loan period can be 12 months
4) Maximum loan period can be 60 months

And uses the following scoring algorithm:

```credit score = (credit modifier / loan amount) * loan period```

which will be used to determine whatever loan will be approved or not and to shift period in some cases, as well as to
determine possible max amount we can approve.