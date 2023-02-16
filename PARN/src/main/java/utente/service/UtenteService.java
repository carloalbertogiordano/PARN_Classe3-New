package utente.service;

import curriculum.service.CurriculumService;
import curriculum.service.CurriculumServiceInterface;
import storage.entity.Azienda;
import storage.entity.Persona;
import storage.entity.Sede;
import storage.entity.Utente;
import utente.dao.UtenteDAO;

import java.sql.SQLException;

public class UtenteService implements UtenteServiceInterface{
    private UtenteDAO utenteDAO;

    public UtenteService(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;}
    public UtenteService(){
        this.utenteDAO = new UtenteDAO();
    }

    @Override
    public Persona getPersonaById(int id) {
        Persona persona = null;
        try{
            persona = utenteDAO.getPersonaById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return persona;
    }

    @Override
    public Azienda getAziendaById(int id) {
        Azienda azienda = new Azienda();
        try{
            azienda = utenteDAO.getAziendaById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return azienda;
    }

    @Override
    public Sede getSedeById(Azienda s,int id) {
            for (Sede se: s.getSedi())
                if (se.getId()==id)
                    return se;
            return null;
        }

    @Override
    public boolean registraPersona(Persona persona) {
        try{
            utenteDAO.addPersona(persona);
        } catch (SQLException e) {
            return false;
        }

        CurriculumServiceInterface curriculumService = new CurriculumService();
        return curriculumService.creaCurriculum(persona.getCurriculum());
    }

    @Override
    public boolean registraAzienda(Azienda azienda) {
        if (!azienda.getMail().matches("^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{2,10}$")) throw new IllegalArgumentException("Mail non vaida");
        if (!azienda.getRegione().matches("^([a-zA-Z]( [a-zA-Z]){0,1}){2,20}$")) throw new IllegalArgumentException("Nome azienda non valido");
        if (!azienda.getProvincia().matches("^[A-Z]{2}$")) throw new IllegalArgumentException("Provincia non valida");
        if (azienda.getCitta().length()>50) throw new IllegalArgumentException("Città non valida");
        if (azienda.getVia().length()>30) throw new IllegalArgumentException("Via non valida");
        if (!azienda.getTelefono().matches("(\\+)([0-9]){2} [0-9]{10}")) throw new IllegalArgumentException("Telefono non valido");
        if (!azienda.getCap().matches("[0-9]{5}")) throw new IllegalArgumentException("Cap non valido");
        if (!azienda.getNome().matches("^([a-zA-Z]( [a-zA-Z]){0,1}){2,20}$")) throw new IllegalArgumentException("Nome non valido");
        if (!azienda.getPartitaIVA().matches("^[0-9]{11}$")) throw new IllegalArgumentException("Partita IVA non valida");
        if (azienda.getRagioneSociale().length()>30) throw new IllegalArgumentException("Ragione Sociale non valida");
        if (!azienda.getLink().matches("^(?=.{1,30})(((http)(s{0,1})(\\:\\/\\/))(w{3})(\\.(([a-zA-Z]){1,})){1,}(\\.{1})([a-z]{1,4}))$")) throw new IllegalArgumentException("Link al sito web non valido");
        if (azienda.getLink().length() > 30) throw new IllegalArgumentException("Link al sito web troppo lungo");
        if (!azienda.getAreaInteresse().matches("^[a-zA-Z]{1,30}$")) throw new IllegalArgumentException("Area Interesse non valida");
        if (!String.valueOf(azienda.getNumeroDipendenti()).matches("^[1-9]{1,}$")) throw new IllegalArgumentException("Numero di dipendenti non valido");
        azienda.getSettoriCompetenza().forEach( str -> {
            if (!str.matches("^[a-zA-Z]{3,30}$")) {
                throw new IllegalArgumentException("Settori di competenza non validi");
            }});
        azienda.getSedi().forEach( sede -> {
            if (!sede.getCitta().matches(".{0,50}")) {throw new IllegalArgumentException("Citta  non valida");}
            if (!sede.getProvincia().matches("^[A-Z]{2}$")) {throw new IllegalArgumentException("Provincia non valida");}
            if (!sede.getCap().matches("[0-9]{5}")) {throw new IllegalArgumentException("CAP  non valido");}
            if (!sede.getTelefono().matches("(\\+)([0-9]){2}\\ [0-9]{10}")) {throw new IllegalArgumentException("Telefono non valido");}
            if (sede.getVia().length() > 30 || sede.getVia().length() == 0) {throw new IllegalArgumentException("Via  non valida");}
            if (!sede.getMail().matches("^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{2,10}$")) {throw new IllegalArgumentException("Sede  non valida");}
            if (sede.getMail().length() > 30) {throw new IllegalArgumentException("Mail  non valida");}
            if (!sede.getRegione().matches("^([a-zA-Z]( [a-zA-Z]){0,1}){2,20}$")) {throw new IllegalArgumentException("Regione non valida");}
        });

        try{
            utenteDAO.addAzienda(azienda);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean registraSede(Sede sede){
        try{
            utenteDAO.addSede(sede);
            return true;
        } catch (SQLException e){
            System.err.println(e);
            return false;
        }
    }
    @Override
    public boolean aggiornaPersona(Persona persona) {
        if(persona.getMail().matches("^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{2,10}$")) {
            if(persona.getRegione().matches("^([a-zA-Z]( [a-zA-Z]){0,1}){2,20}$")) {
                if(persona.getProvincia().matches("^[A-Z]{2}$")){
                    if(persona.getCap().matches("[0-9]{5}")) {
                        if(persona.getTelefono().matches("(\\+)([0-9]){2} [0-9]{10}")){
                            if(persona.getCitta().length() >= 2 && persona.getCitta().length() < 50){
                                if(persona.getVia().length() >= 2 && persona.getVia().length() < 30) {
                                    if(Persona.FILTRO_MACROAREA.contains(persona.getFiltroMacroarea())) {
                                        if(persona.getPosizioneDesiderata().length() >= 2 && persona.getPosizioneDesiderata().length() < 30) {
                                                return utenteDAO.aggiornaPersona(persona);
                                        } else throw new IllegalArgumentException("La lunghezza della posizione desiderata non è corretta");

                                    } else throw new IllegalArgumentException("Filtro macroarea non valido");

                                } else throw new IllegalArgumentException("La lunghezza della via non è corretta");

                            } else throw new IllegalArgumentException("La lunghezza della città non è corretta");

                        } else throw new IllegalArgumentException("Il telefono non rispetta il formato");

                    } else throw new IllegalArgumentException("Il CAP non rispetta il formato");

                } else throw new IllegalArgumentException("La provincia non rispetta il formato");

            } else throw new IllegalArgumentException("La regione non rispetta il fomrato");

        } else throw new IllegalArgumentException("La mail non non rispetta il formato");

    }

    @Override
    public boolean aggiornaAzienda(Azienda azienda) {
        try{
            utenteDAO.aggiornaAzienda(azienda);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean aggiornaSede(Sede sede) {
        try{
            utenteDAO.aggiornaSede(sede);
        }catch (SQLException e){
            return false;
        }

        return true;
    }

    @Override
    public boolean aggiornaUtente(Utente utente) {
        try{
            utenteDAO.aggiornaUtente(utente);
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    @Override
    public Utente autenticazione(String mail, String password) {
        Utente utente;
        try{
            utente = utenteDAO.autenticazione(mail, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utente;
    }

    /**
     * @param azienda 
     * @return
     */
    @Override
    public boolean eliminaAzienda(Azienda azienda) {
        try {
            utenteDAO.eliminaUtenteById(azienda.getId());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * @param persona 
     * @return
     */
    @Override
    public boolean eliminaPersona(Persona persona) {
        try {
            utenteDAO.eliminaUtenteById(persona.getId());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }



}
