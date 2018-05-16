peace(0,[x,x,x]).
peace(1,[b,r,t]).
peace(2,[b,r,s]).
peace(3,[b,s,t]).
peace(4,[b,s,s]).
peace(5,[w,r,t]).
peace(6,[w,r,s]).
peace(7,[w,s,t]).
peace(8,[w,s,s]).

bestMove(Pos,[H|T],Holder,Final):-
    minimax(Pos,H, Holder, Val),
    winning(Holder,Val),copy(Holder,Final),!;
    length(T,N),N=1,minimax(Pos,H, Holder, Val),copy(Holder,Final),!;
    bestMove(Pos,T,Holder2,Final).

humanPlay :- 
    open('Choice.txt',read, Stream1),
    read(Stream1,Chosen),
    close(Stream1),
    open('Pos.txt',read, Stream2),
    read(Stream2,Pos),
    close(Stream2),
    open('Next.txt',read, Stream3),
    read(Stream3,Board),
    close(Stream3),
    humanMove(Chosen,[h, play, Board], [NextPlayer, State, NextBoard], Pos),
    open('Next.txt',write, Stream4),
    write(Stream4,'[c,'),
    write(Stream4,State),
    write(Stream4,','),
    write(Stream4,NextBoard),
    write(Stream4,']'),
    write(Stream4,'.'),
    close(Stream4),!.

computerPlay:-
    open('Next.txt',read, Stream1),
    read(Stream1,Board),
    close(Stream1),
    open('Peaces.txt',read, Stream),
    read(Stream,L),
    close(Stream),
    readPeaces(L,NewList),
    bestMove([c, play, Board],NewList,Temp,[NextPlayer, State, BestSuccBoard]),
    open('Next.txt',write, Stream2),
    write(Stream2,'[h,'),
    write(Stream2,State),
    write(Stream2,','),
    write(Stream2,BestSuccBoard),
    write(Stream2,']'),
    write(Stream2,'.'),
    close(Stream2),!.

nextPlayer(c,h).

nextPlayer(h, c).

humanMove(Chosen,[X1, play, Board], [X2, State, NextBoard], Pos) :-
    nextPlayer(X1, X2),
    set1(Pos, Chosen, Board, NextBoard),
    (
      winPos(X1, NextBoard), !, State = win ;
      drawPos(X1,NextBoard), !, State = draw ;
      State = play
    ).

copy(L,R) :- accCp(L,R).
accCp([],[]).
accCp([H|T1],[H|T2]) :- accCp(T1,T2).
readPeaces(L,List):-
   findall(Y,permutation(L, Y),List).
set1(1, E, [X|Ls], [E|Ls]) :- 
  !, X = 0.


set1(P, E, [X|Ls], [X|L2s]) :-
  number(P),
  P1 is P - 1,
  set1(P1, E, Ls, L2s).

removehead([_|Tail], Tail).


minimax(Pos,Peaces,BestNextPos, Val) :-
        bagof(NextPos, move(Peaces,Pos, NextPos), NextPosList),
        removehead(Peaces,Tail),
        best(NextPosList,Tail,BestNextPos, Val),!.

minimax(Pos,_, _, Val) :-
        utility(Pos, Val).


best([Pos],Peaces,Pos, Val) :-
        minimax(Pos,Peaces, _, Val), !.

best([Pos1 | PosList],Peaces, BestPos, BestVal) :-
        minimax(Pos1,Peaces, _, Val1),
        best(PosList, Peaces,Pos2, Val2),
        betterOf(Pos1, Val1, Pos2, Val2, BestPos, BestVal).

betterOf(Pos0, Val0, _, Val1, Pos0, Val0) :-   % Pos0 better than Pos1
        min_to_move(Pos0),                         % MIN to move in Pos0
        Val0 > Val1, !                             % MAX prefers the greater value
        ;
        max_to_move(Pos0),                         % MAX to move in Pos0
        Val0 < Val1, !.                            % MIN prefers the lesser value

betterOf(_, _, Pos1, Val1, Pos1, Val1).        % Otherwise Pos1 better than Pos0


move([P|Peaces],[X1, play, Board],[X2, win, NextBoard]) :-

        nextPlayer(X1, X2),
        \+member(P,Board),
        move_aux(P, Board, NextBoard),
        winPos(X1, NextBoard),!.

nextPlayer(h,c).
nextPlayer(c,h).
winning([P,State,List],VAL):-
    State = win,VAL= -1,!;
    State = play,count(List,C),C < 3,VAL= -1.


move([P|Peaces],[X1, play, Board],[X2, draw, NextBoard]) :-
        nextPlayer(X1, X2),
        \+member(P,Board),
        move_aux(P, Board, NextBoard),
        drawPos(X1,NextBoard),!.


       
move([P|Peaces],[X1, play, Board], [X2, play, NextBoard]) :-
        nextPlayer(X1, X2),
        \+member(P,Board),
        move_aux(P, Board, NextBoard).


move_aux(P, [0|Bs], [P|Bs]).


move_aux(P, [B|Bs], [B|B2s]) :-

move_aux(P, Bs, B2s).


min_to_move([c, _, _]).

max_to_move([h, _, _]).


utility([c, win, _], 1).

utility([h, win, _], -1).
       
utility([_, draw, _], 0).

check(P1,P2,P3):-

   nth0(0,P1, P11),nth0(0,P2, P21),nth0(0,P3, P31),
   P11=P21,P21=P31,P11\=x,!;

   nth0(1,P1, P12),nth0(1,P2, P22),nth0(1,P3, P32),
   P12=P22,P22=P32,P12\=x,!;

   nth0(2,P1, P13),nth0(2,P2, P23),nth0(2,P3, P33),
   P13=P23,P23=P33,P13\=x.

winPos(P, [X1, X2, X3, X4, X5, X6, X7, X8, X9]) :-

    peace(X1,P1),peace(X2,P2),peace(X3,P3),check(P1,P2,P3),!;

    peace(X4,P4),peace(X5,P5),peace(X6,P6),check(P4,P5,P6),!;

    peace(X7,P7),peace(X8,P8),peace(X9,P9),check(P7,P8,P9),!;

    peace(X1,P1),peace(X4,P4),peace(X7,P7),check(P1,P4,P7),!;

    peace(X2,P2),peace(X5,P5),peace(X8,P8),check(P2,P5,P8),!;

    peace(X3,P3),peace(X6,P6),peace(X9,P9),check(P3,P6,P9),!;

    peace(X1,P1),peace(X5,P5),peace(X9,P9),check(P1,P5,P9),!;

    peace(X3,P3),peace(X5,P5),peace(X7,P7),check(P3,P5,P7).

count([],0).

count([0|T],N) :-
        count(T,N).

count([X|T],N) :-
        count(T,N1),!,
        N is N1 + 1.

drawPos(_,Board) :-
   member(0,Board),
   member(1,Board),
   member(2,Board),
   member(3,Board),
   member(4,Board),
   member(5,Board),
   member(6,Board),
   member(7,Board),
   member(8,Board).

              
            
