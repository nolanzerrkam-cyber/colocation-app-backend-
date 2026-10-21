package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

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

    public void addExpense (ExpenseEntity expense) {
        if (expense != null && !expenses.contains(expense)) {
            expenses.add(expense);
            if (expense.getRoomate() != this) {
                expense.setRoomate(this);
            }
        }
    }

    public void removeExpense (ExpenseEntity expense) {
            expenses.remove(expense);
    }
    @Override
    public String toString()
    {
        return String.format("User [email=%s, password=%s, firstname=%s, lastname=%s, phonenumber=%s]", getEmail(),getPassword() ,getFirstname(), getLastname(),getPhonenumber());
    }

    // faire fonctione qui calcule les depense d'un user

    // faire DTO qui calcule tout les depense de tout les user

}
