package be.dikkenek.colocationbackend.dto;

import java.util.List;

public class ListExpenseResponseDto {
    private List<GetExpenseDto> expensesdto;
    private float totalExpense;

    public ListExpenseResponseDto(float totalExpense, List<GetExpenseDto> expensesdto) {
        setExpensesdto(expensesdto);
        setTotalExpense(totalExpense);
    }

    public List<GetExpenseDto> getExpensesdto() {
        return expensesdto;
    }

    public void setExpensesdto(List<GetExpenseDto> expensesdto) {
        this.expensesdto = expensesdto;
    }

    public float getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(float totalExpense) {
        this.totalExpense = totalExpense;
    }
}
