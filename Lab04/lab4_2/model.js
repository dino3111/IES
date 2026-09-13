class Model {
    constructor() {
        this.todos = JSON.parse(localStorage.getItem('todos')) || [];
    }

    bindTodoListChanged(callback) {
        this.onTodoListChanged = callback;
    }

    save(todos) {
        this.onTodoListChanged(todos);
        localStorage.setItem('todos', JSON.stringify(todos));
    }

    create(todoName) {
        const todo = {
            id: this.todos.length > 0 ? this.todos[this.todos.length - 1].id + 1 : 1,
            name: todoName,
            status: false,
        };

        this.todos.push(todo);
        this.save(this.todos);
    }

    update(id, todoName) {
        this.todos = this.todos.map((todo) =>
            todo.id === id
                ? { id: todo.id, name: todoName, status: todo.status }
                : todo
        );

        this.save(this.todos);
    }

    remove(id) {
        this.todos = this.todos.filter((todo) => todo.id !== id);
        this.save(this.todos);
    }

    toggleStatus(id) {
        this.todos = this.todos.map((todo) =>
            todo.id === id
                ? { id: todo.id, name: todo.name, status: !todo.status }
                : todo
        );

        this.save(this.todos);
    }

    onAddTodo(handler) {
        this.form.addEventListener('submit', e => {
            e.preventDefault();

            if (this.todoText) {
                handler(this.todoText);
                this.resetInput();
            }
        });
    }

    onDeleteTodo(handler) {
        this.todoList.addEventListener('click', e => {
            if (e.target.className === 'todo-button--delete') {
                handler(parseInt(e.target.parentElement.id));
            }
        });
    }

    onEditTodo(handler) {
        this.todoList.addEventListener('focusout', e => {
            if (this.temporaryTodoName) {
                handler(
                    parseInt(e.target.parentElement.id),
                    this.temporaryTodoName
                );
                this.temporaryTodoName = '';
            }
        });
    }

    onToggleTodo(handler) {
        this.todoList.addEventListener('change', e => {
            if (e.target.type === 'checkbox') {
                handler(parseInt(e.target.parentElement.id));
            }
        });
    }
}

export default Model;
