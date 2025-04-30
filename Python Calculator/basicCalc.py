import tkinter as tk

def evaluate_expression():
    try:
        expression = entry.get()
        result = eval(expression)
        
        entry.delete(0, tk.END) 
        entry.insert(0, f"{expression} = {result}")
    except Exception as e:
        entry.delete(0, tk.END) 
        entry.insert(0, "Error")

def button_click(value):
    current = entry.get()
    entry.delete(0, tk.END)  
    entry.insert(0, current + str(value))

def create_calculator():
    window = tk.Tk()
    window.title("Basic Calculator")
    
    global entry
    entry = tk.Entry(window, width=30, font=("Arial", 14))
    entry.grid(row=0, column=0, columnspan=4, padx=10, pady=10)

    # Button labels / definitions
    buttons = [
        ('7', 1, 0), ('8', 1, 1), ('9', 1, 2), ('/', 1, 3),
        ('4', 2, 0), ('5', 2, 1), ('6', 2, 2), ('*', 2, 3),
        ('1', 3, 0), ('2', 3, 1), ('3', 3, 2), ('-', 3, 3),
        ('0', 4, 0), ('C', 4, 1), ('=', 4, 2), ('+', 4, 3),
    ]

    for (text, row, column) in buttons:
        if text == '=':
            button = tk.Button(window, text=text, width=10, height=2, command=evaluate_expression)
        elif text == 'C':
            button = tk.Button(window, text=text, width=10, height=2, command=lambda: entry.delete(0, tk.END))
        else:
            button = tk.Button(window, text=text, width=10, height=2, command=lambda value=text: button_click(value))
        
        button.grid(row=row, column=column, padx=5, pady=5)

    
    window.mainloop()

create_calculator()