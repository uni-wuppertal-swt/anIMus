package org.watzlawek.models;

import java.util.Vector;

/**
 * Diese Klasse verwaltet die Gruppen. Es existiert nur ein Objekt zur Gruppenverwaltung (Singleton).
 * 
 * @author Safran Quader
 * 
 * @version 2015-01-30
 */
public class Grouplist {
	
	private static Vector<Group> mGrouplist;
	private static Grouplist mInstance;
	
	// Der Konstruktor ist leer, um die irrtümliche Initialisierung eines zweiten Objektes zu verhindern
	private Grouplist() {
		
	}
	
	/**
	 * Gibt die Instanz der Gruppenliste zurück. Initialisiert eine leere Gruppenliste, falls noch keine vorhanden ist.
	 * @return Gruppenliste
	 */
	public static Grouplist getInstance() {
		if (Grouplist.mInstance == null) {
			mGrouplist = new Vector<Group>();
		}
		
		return Grouplist.mInstance;
	};
	
	/**
	 * Fügt eine Gruppe der Gruppenliste hinzu.
	 * @param group
	 */
	public void addGroup(Group group) {
		mGrouplist.add(group);
	}
	
	/**
	 * Entfernt eine Gruppe aus der Gruppenliste.
	 * @param id
	 */
	public void deleteGRoup(int id) {
		mGrouplist.remove(id);
	}
	
	/**
	 * Liefert eine Gruppe aus der Gruppenliste zurück
	 * @param id
	 * @return Gruppe
	 */
	public Group getGroup(int id) {
		return mGrouplist.get(id);
	}
	
	/**
	 * Gibt die Größe der Gruppenliste aus.
	 * @return Anzahl der Einträge in der Gruppenliste
	 */
	public int size() {
		return mGrouplist.size();
	}
}
