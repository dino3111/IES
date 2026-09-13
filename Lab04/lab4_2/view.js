class View {
    constructor() {
        this.app = document.querySelector('#app');

        this.app.innerHTML = `
      <h1 class="todo-title">Todos</h1>
      <form class="todo-form">
        <input type="text" class="todo-form__input" placeholder="Add todo" name="todo">
        <button class="todo-button">Add</button>
      </form>
      <ul class="todo-list"></ul>
    `;

        this.form = this.app.querySelector('.todo-form');
        this.input = this.app.querySelector('.todo-form__input');
        this.todoList = this.app.querySelector('.todo-list');

        this.temporaryTodoName = '';
        this.initLocalListeners();
    }

    get todoText() {
        return this.input.value;
    }

    resetInput() {
        this.input.value = '';
    }

    render(todos) {
        if (todos.length === 0) {
            this.todoList.innerHTML = '<p>Nothing to do! Good Job 🏆</p>';
            return;
        }

        this.todoList.innerHTML = todos.map(todo => `
      <li class="todo-item" id="${todo.id}">
        <input type="checkbox" class="todo-item__checkbox" ${todo.status ? 'checked' : ''}>
        <span class="todo-item__text editable" contenteditable="true">
          ${todo.status ? `<s>${todo.name}</s>` : todo.name}
        </span>
        <button class="todo-button--delete">Delete</button>
      </li>
    `).join('');
    }

    initLocalListeners() {
        this.todoList.addEventListener('input', e => {
            if (e.target.classList.contains('editable')) {
                this.temporaryTodoName = e.target.innerText;
            }
        });
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

export default View;