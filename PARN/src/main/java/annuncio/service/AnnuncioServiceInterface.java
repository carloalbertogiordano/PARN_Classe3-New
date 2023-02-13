package annuncio.service;

import storage.entity.Annuncio;
import storage.entity.Candidatura;

import java.sql.*;
import java.util.List;

public interface AnnuncioServiceInterface {

    public List<Annuncio> getAnnuncioById(int id) throws SQLException;
    public boolean creaAnnuncio(Annuncio annuncio);
    public boolean modificaAnnuncio(Annuncio annuncio);
    public boolean eliminaAnnuncio(Annuncio annuncio);
    public boolean chiusuraAnnuncio(Annuncio annuncio);
    public boolean aggiungiCandidatura(Annuncio annuncio, Candidatura candidatura);
    public boolean rimuoviCandidatura(Annuncio annuncio, Candidatura candidatura);
    public List<Candidatura> visualizzaCandidatura(Annuncio annuncio);

    List<Annuncio> getAnnunciByStato(String in_corso);
}
