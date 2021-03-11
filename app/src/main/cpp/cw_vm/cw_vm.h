#define DELAY 500
#define CYCLES_BEFORE_TIE 80000
#define CORE_SIZE 8000
#define MAX_TASKS 8000
#define RW_RANGE 500
#define WARRIORS 2 // Пока что, игра будет приспособлена только для двух программ

#define MIN_SEPARATION 100 

// Можно сделать функцию, которая будет случайно выбирать место спавна первой проги и 
// выбора случайной точки с последующей проверкой на условие (далее приведены псевдопеременные):

//              warrior2.start_address < warrior1.start_address + warrior1.instructions_count
//                  ||
//              warrior1.start_address < warrior2.start_address + warrior2.instructions_count


// Я вот думаю, стоит ли создавать MemoryArray в виде массива этих самых инструкций?
    //int warrior; // Кому принадлежит данная ячейка памяти; думаю, может быть полезно для отображения разных программ
struct cw_instruction {
    int opcode;
    int a_operand;
    int b_operand;
}

struct cw_vm_task_state {

}

struct cw_vm_task {
    struct cw_vm_task_state task_state;
    int instruction_pointer; // Где находится нужная нам инструкция
}

struct cw_warrior {
    //struct cw_task_queue task_queue;
}

struct cw_vm_state {
    // Состояние машины
}

struct cw_vm {
    struct cw_vm_state vm_state;
    struct cw_warrior_queue warrior_queue; // Очередь из исполнителей
}

// Если у нас есть два исполнителя: warrior1 с одним таском, а warrior2 - с двумя, то по идее, так они должны вместе работать:

// warrior1.task[0]
// warrior2.task[0]
// warrior1.task[0]
// warrior2.task[1]
// и т.д. ...

void * initialize_core();
void place_warriors();
