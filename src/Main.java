import entities.User;
import functional_interfaces.StringModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Main {
	public static void main(String[] args) {
		// Lambda function () -> {}
		StringModifier starsWrapper = str -> "*" + str + "*";
		StringModifier dotsWrapper = str -> "..." + str + "...";
		// Le 2 lambda functions vanno ad implementare l'unico metodo dell'interfaccia funzionale StringModifier

		System.out.println(starsWrapper.modify("CIAO"));
		System.out.println(dotsWrapper.modify("CIAO"));


		StringModifier stringInverter = stringa -> {
			String[] splitted = stringa.split("");
			String inverted = "";

			for (int i = splitted.length - 1; i >= 0; i--) {
				inverted += splitted[i];
			}
			return inverted;
		};

		System.out.println(stringInverter.modify("CIAO"));

		// ***************************** PREDICATES *************************************
		Predicate<Integer> isMoreThanZero = num -> num > 0;
		Predicate<Integer> isLessThanHundred = num -> num < 100;
		Predicate<User> isAgeLessThanFifty = user -> user.getAge() < 50;

		User aldo = new User("Aldo", "Baglio", 18);

		System.out.println(isMoreThanZero.test(aldo.getAge()));
		System.out.println(isMoreThanZero.and(isLessThanHundred).test(aldo.getAge()));
		System.out.println(isAgeLessThanFifty.negate().test(aldo));


		// ***************************** SUPPLIERS *************************************
		Supplier<Integer> randomNumbersSupplier = () -> {
			Random rndm = new Random();
			return rndm.nextInt(1, 1000);
		};

		List<Integer> numeriRandom = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			numeriRandom.add(randomNumbersSupplier.get());
		}

		System.out.println(numeriRandom);

		Supplier<User> userSupplier = () -> new User("NOME", "COGNOME", randomNumbersSupplier.get());

		List<User> users = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			users.add(userSupplier.get());
		}

		System.out.println(users);

		Supplier<List<User>> randomList = () -> {
			List<User> list = new ArrayList<>();
			for (int i = 0; i < 100; i++) {
				list.add(userSupplier.get());
			}
			return list;
		};

		System.out.println(randomList.get());
		System.out.println(randomList.get());

		users.forEach(user -> System.out.println(user));
		// users.forEach(System.out::println); // <-- Identica alla riga precedente però leggermente più compatta

		// **************************************************** STREAMS - FILTER *******************************************************
		List<Integer> randomIntegers = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			randomIntegers.add(randomNumbersSupplier.get());
		}

		System.out.println("Elenco dei numeri compresi tra 0 e 50");
/*		Stream<Integer> stream = randomIntegers.stream().filter(num -> num > 0 && num < 10);
		stream.forEach(num -> System.out.println(num));*/
		randomIntegers.stream().filter(num -> num > 0 && num < 10).forEach(System.out::println);
		// Gli stream vanno sempre "aperti" con .stream(), poi posso farci le operazioni intermedie tipo .filter(), ma alla fine devo ricordarmi
		// di concludere lo stream. Concludere lo stream nel caso di sopra vuol dire usare .forEach per stampare a video il risultato

		// Nei filter posso utilizzare dei Predicate se li ho già definiti in precedenza
		randomIntegers.stream().filter(isMoreThanZero.and(isLessThanHundred)).forEach(System.out::println);
		users.stream().filter(isAgeLessThanFifty).forEach(System.out::println);


		// **************************************************** STREAMS - MAP *******************************************************
		System.out.println("**************************************************** STREAMS - MAP *******************************************************");
		users.stream().map(user -> user.getAge()).forEach(age -> System.out.println(age));
		// users.stream().map(User::getAge).forEach(System.out::println); <-- equivalente alla riga precedente

		System.out.println("**************************************************** STREAMS - FILTER & MAP *******************************************************");
		users.stream().filter(user -> user.getAge() < 18).map(user -> user.getAge()).forEach(age -> System.out.println(age));


		// **************************************************** STREAMS - REDUCE *******************************************************
		System.out.println("**************************************************** STREAMS - REDUCE *******************************************************");
		int totale = users.stream() // <-- Siccome il Reduce è un metodo terminatore di stream, allora non otterrò come prima uno Stream ma in questo caso un numero intero
				.filter(user -> user.getAge() < 18)
				.map(user -> user.getAge())
				.reduce(0, (partialSum, currentNumber) -> partialSum + currentNumber);

		System.out.println("La somma delle età dei minorenni è: " + totale);

		// ************************************************* OTTENERE UNA NUOVA LISTA DA UNO STREAM *********************************
		List<User> utentiMinorenni = users.stream().filter(user -> user.getAge() < 18).toList(); // .toList() termina lo Stream fornendoci una nuova lista
		System.out.println(utentiMinorenni);
		// Esso è un'alternativa più compatta e pratica al .collect(Collectors.toList())
		/*List<User> utentiMinorenni = users.stream().filter(user -> user.getAge() < 18).collect(Collectors.toList());*/

		List<Integer> listaEtàMinorenni = users.stream().filter(user -> user.getAge() < 18).map(user -> user.getAge()).toList();
		System.out.println(listaEtàMinorenni);

		List<String> listaNomiMinorenni = users.stream()
				.filter(user -> user.getAge() < 18)
				.map(user -> user.getFirstName() + " " + user.getLastName())
				.toList();
		
		System.out.println(listaNomiMinorenni);

	}
}

/*

Situazione pre lamdba functions:
avrei dovuto crearmi una classe per ogni funzionalità per avere i metodi desiderati
poi nel main avrei dovuto creare degli oggetti per poter usare i metodi

Con le lambda functions invece con una riga ho risolto

public class StarsModiferImpl implements StringModifier {

	@Override
	public String modify(String stringa) {
		return "*" + stringa + "*";
	}
}

public class DotsModifierImpl implements StringModifier {

	@Override
	public String modify(String stringa) {
		return "..." + stringa + "...";
	}
}*/
