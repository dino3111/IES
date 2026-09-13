function useState(initialValue) {
    let stateValue = initialValue;

    function setState(newValue) {
        stateValue =
            typeof newValue === 'function'
                ? newValue(stateValue)
                : newValue;

        updateStateDisplay();
    }

    const getState = () => stateValue;

    return [getState, setState];
}

const app = document.getElementById('app');

const input = document.createElement('input');
input.placeholder = 'Type a task...';

const addBtn = document.createElement('button');
addBtn.textContent = 'Add Task';

const list = document.createElement('ul');

// Debugger
const stateDisplay = document.createElement('pre');
stateDisplay.style.background = '#f4f4f4';
stateDisplay.style.padding = '10px';
stateDisplay.style.marginTop = '20px';

// Inject UI
app.append(input, addBtn, list, stateDisplay);

// STATE
const [getState, setState] = useState({ tasks: [] });

function updateStateDisplay() {
    stateDisplay.textContent =
        'CURRENT MEMORY (STATE):\n' +
        JSON.stringify(getState(), null, 2);
}

updateStateDisplay();

// ADD TASK
addBtn.addEventListener('click', () => {
    if (input.value === '') return;

    const taskId = Date.now();
    const taskText = input.value;

    // Update STATE
    setState(prev => ({
        tasks: [...prev.tasks, { id: taskId, text: taskText }]
    }));

    // Create DOM manually
    const li = document.createElement('li');
    li.textContent = taskText + ' ';

    // EDIT
    const editBtn = document.createElement('button');
    editBtn.textContent = 'Edit';

    editBtn.addEventListener('click', () => {
        const currentTask = li.firstChild.nodeValue.trim();
        const newTask = prompt('Edit task:', currentTask);

        if (newTask !== null && newTask.trim() !== '') {
            li.firstChild.nodeValue = newTask + ' ';

            setState(prev => ({
                tasks: prev.tasks.map(t =>
                    t.id === taskId ? { ...t, text: newTask } : t
                )
            }));
        }
    });

    // BUTTON 1
    const domDeleteBtn = document.createElement('button');
    domDeleteBtn.textContent = 'Erase from Screen Only';

    domDeleteBtn.addEventListener('click', () => {
        li.remove();
    });

    // BUTTON 2
    const stateDeleteBtn = document.createElement('button');
    stateDeleteBtn.textContent = 'Erase from Memory Only';

    stateDeleteBtn.addEventListener('click', () => {
        setState(prev => ({
            tasks: prev.tasks.filter(t => t.id !== taskId)
        }));
    });

    li.appendChild(editBtn);
    li.appendChild(domDeleteBtn);
    li.appendChild(stateDeleteBtn);
    list.appendChild(li);

    input.value = '';
});