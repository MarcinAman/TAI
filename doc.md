#TAI
##Wymagania funkcjonalne
+ Wykresy dla walut z tabel NBP.
+ Estymacja zysku z inwestycji z użyciem Sparka
+ Persystencja obserwowanych walut pomiędzy sesjami użytkownika.
##Wymagania niefunkcjonalne
+ Travis pipeline (testy na pull-request'ach)
+ Docker compose do deployowania obrazu backendu i frontendu osobno
+ Aplikacja deployowana na digital ocean i dostępna przez domenę przydzieloną przez ngrok.
+ Osobne testy automatyczne: Selenium (dla dwóch kluczowych funkcjonalności 66% coverage)
##Podsumowanie
###Problemy implementacyjne
+ Spark
+ Zgranie mongo z graphQL'em w silnie typowanym języku,
+ Obsługa nowo poznanej technologii graphQL.
+ Ustalenie stabilnej wersji bibliotek dla frontendu.
###Problemy infrastrukturalne
Zasadniczo skomplikowaliśmy sobie życie tworząc zgodnie ze sztuką osobne kontenery dla 
frontendu i backendu naszej aplikacji. W wyniku tego rozdzielenia skorzystanie z Heroku płatnego plugina 
dla docker composea okazało się niemożliwe. Dlatego one click deployment był poza naszym zasięgiem 
tak samo jak dodanie deploymentu do Travis pipeline. Sam pipeline również nie wspierał łatwo testów selenium,
dlatego zostały one wyłączone. Jedynie testy funkcjonalności 
napisane w scalatest były odpalane razem z budowaniem projektu. 

Do budowania wykorzystaliśmy skrypty bashowe po których wykonaniu można 
było publishować obrazy dockerowe do Docker Huba i stamtąd pobierać je na serwer produkcyjny.
można było także odpalić zbudowane obrazy lokalnie jeśli zdecydowalibyśmy się budować projekt na serwerze.

Jeśli chodzi o testy z wykorzystaniem selenium, okazało się że Selenium IDE 
pomimo że oferuje nagrywanie testów i generowanie na ich podstawie kodu java wymagało napisania testów zupełnie od nowa.
Ponieważ ścieżki i ogólne zachowanie aplikacji w Selenium IDE nie odpowiadało zachowaniu testów z generowanego kodu.
Zwróciliśmy także uwagę że selenium do działania potrzebuje window managera tak więc jedynym rozwiązaniem 
żeby ddoać go do pipelineu byoby dodanie odpowiedniego obrazu 
dokerowego (https://github.com/SeleniumHQ/docker-selenium) i wystartowanie 
testów wewnątrz niego.
