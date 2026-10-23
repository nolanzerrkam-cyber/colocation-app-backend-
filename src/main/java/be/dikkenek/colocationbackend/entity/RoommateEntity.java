package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.ExpenseDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
classe roomate (colocataire) qui permet de créer un colocataire :
attribut : - email, password, firstname, lastname, phonenumber (heriter de la classe user)
           - expenses (liste de depense)
*/
@Entity(name = "Roommate")
public class RoommateEntity extends UserEntity
{

    @OneToMany(mappedBy = "roomate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseEntity> expenses;

    public RoommateEntity() {
        setExpenses(new ArrayList<>());
    }

    public RoommateEntity(String email, String password, String firstname, String lastname, String phonenumber) throws BackendException {
        super(email, password, firstname, lastname, phonenumber);
        setExpenses(new ArrayList<>());

    }

    public List<ExpenseEntity> getExpenses(){
        return expenses;
    }

    public void setExpenses(List<ExpenseEntity> expenses)
    {
        this.expenses = expenses;
    }

    public void addExpense (ExpenseEntity expense)
    {
        //methode qui ajoute une depense dans la liste
        if (expense != null && !expenses.contains(expense)) {
            expenses.add(expense);
            if (expense.getRoomate() != this) {
                expense.setRoomate(this);
            }
        }
    }

    public void removeExpense (ExpenseEntity expense)
    {
        // methode qui supprime une depense de la liste
        expenses.remove(expense);
    }

    @Override
    public String toString()
    {
        return String.format("User [email=%s, password=%s, firstname=%s, lastname=%s, phonenumber=%s]", getEmail(),getPassword() ,getFirstname(), getLastname(),getPhonenumber());
    }

    public float getPriceRoomate()
    {
        // fonction qui calcule le montant total des depense d'un coloc
        return expenses.stream().map(ExpenseEntity::getPrice).reduce(0.0f, Float::sum);
    }

    public static float getTotalExpense(List<RoommateEntity> roommates)
    {
        // fonction qui calcule l'ensemble des depense de la colocation
        float totalGlobal = roommates.stream()
                .flatMap(roommate -> roommate.getExpenses().stream())
                .map(ExpenseEntity::getPrice)
                .reduce(0.0f, Float::sum);
        return totalGlobal;
    }

    public static List<RoommateEntity> findAll(UserDao userDao) throws BackendException {
        return userDao.getAll().stream()
                .filter(user -> user instanceof RoommateEntity)
                .map(RoommateEntity.class::cast).collect(Collectors.toList());
    }

}
