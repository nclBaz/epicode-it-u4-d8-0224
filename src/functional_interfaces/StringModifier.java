package functional_interfaces;

@FunctionalInterface
public interface StringModifier {
	public String modify(String stringa);
	/*public void ciao();*/ // <- Un'interfaccia funzionale è tale se ha uno e uno solo metodo astratto
}
