package org.watzlawek.models;

import java.util.Vector;

import android.util.Log;

/**
 * Diese Klasse verwaltet die Gruppen. Es existiert nur ein Objekt zur Gruppenverwaltung (Singleton).
 * 
 * @author Safran Quader
 * 
 * @version 2015-01-30
 */
public class Grouplist {
	
	private static final String TAG = "Grouplist";
	
	private static Vector<Group> mGrouplist;
	private static Grouplist mInstance;
	
	// Der Konstruktor ist leer, um die irrtümliche Initialisierung eines weiteren Objektes zu verhindern
	private Grouplist() {
		Log.e(TAG, "Singleton: Es wird versucht ein weiteres Objekt zu instanziieren.");
	}
	
	/**
	 * Gibt die Instanz der Gruppenliste zurück. Initialisiert eine leere Gruppenliste, falls noch keine vorhanden ist.
	 * @return Gruppenliste
	 */
	public static Grouplist getInstance() {
		if (Grouplist.mInstance == null) {
			mGrouplist = new Vector<Group>();
		}
		
		Log.d(TAG, "Instanz wird bereitgestellt");
		
		return Grouplist.mInstance;
	};
	
	/**
	 * Fügt eine Gruppe der Gruppenliste hinzu.
	 * @param group
	 */
	public void addGroup(Group group) {
		mGrouplist.add(group);
		Log.d(TAG, "Gruppe hinzugefügt");
	}
	
	/**
	 * Entfernt eine Gruppe aus der Gruppenliste.
	 * @param id
	 */
	public void deleteGRoup(int id) {
		mGrouplist.remove(id);
		Log.d(TAG, "Gruppe gelöscht");
	}
	
	/**
	 * Liefert eine Gruppe aus der Gruppenliste zurück
	 * @param id
	 * @return Gruppe
	 */
	public Group getGroup(int id) {
		Log.d(TAG, "Stelle Gruppe bereit: " + mGrouplist.toString());
		return mGrouplist.get(id);
	}
	
	/**
	 * Gibt die Größe der Gruppenliste aus.
	 * @return Anzahl der Einträge in der Gruppenliste
	 */
	public int size() {
		Log.d(TAG, "Einträge in Gruppenliste: " + mGrouplist.size());
		return mGrouplist.size();
	}
}
