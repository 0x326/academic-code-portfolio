#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct Variable {
    char *name;
    char *value;

    struct Variable *next;
};

struct Scope {
    struct Variable *variables;

    struct Scope *previous;
};

/**
 * Append elem to list
 * @param list The list to append to
 * @param elem The element to append
 */
void AppendVariable(struct Variable *list, struct Variable *elem) {
    // Find last Variable
    while (list->next != NULL) {
        list = list->next;
    }

    list->next = elem;
}

/**
 * Find the Variable in the current scope (or its parent scopes)
 * @param variable_name The name of the variable to find
 * @param scope The scope to search
 * @return The Variable or NULL
 */
struct Variable *FindVariable(char *variable_name, struct Scope *scope) {
    while (scope != NULL) {
        struct Variable *variable = scope->variables;
        while (variable != NULL) {
            if (strcmp(variable->name, variable_name) == 0) {
                return variable;
            }

            variable = variable->next;
        }
        scope = scope->previous;
    }
    return NULL;
}

#define TOKEN_LENGTH 32

int main() {
    struct Scope *current_scope = NULL;
    char token[TOKEN_LENGTH];
    while (1) {
        printf("> ");
        if (scanf("%32s", token) == EOF) {
            printf("\n");
            break;
        } else if (strcmp("DECLARE", token) == 0) {
            if (current_scope == NULL) {
                printf("Error: You must enter a scope before declaring variables\n");
                // Consume tokens
                scanf("%32s %32s", token, token);
                continue;
            }

            // Dynamically allocate character arrays since we need them to persist
            char *variable_name = malloc(sizeof(char) * TOKEN_LENGTH);
            char *variable_value = malloc(sizeof(char) * TOKEN_LENGTH);

            scanf("%32s %32s", variable_name, variable_value);

            // Allocate a Variable
            struct Variable *variable = malloc(sizeof(struct Variable));
            variable->name = variable_name;
            variable->value = variable_value;
            variable->next = NULL;

            if (current_scope->variables == NULL) {
                current_scope->variables = variable;
            } else {
                AppendVariable(current_scope->variables, variable);
            }
        } else if (strcmp("SET", token) == 0) {
            // Get variable name
            char variable_name[TOKEN_LENGTH];
            scanf("%32s", variable_name);

            // Set variable value
            struct Variable *variable = FindVariable(variable_name, current_scope);
            if (variable != NULL) {
                // Read in new value directly to pre-allocated character array
                scanf("%32s", variable->value);
            } else {
                printf("Error: Cannot find variable '%s'\n", variable_name);
                // Consume token
                scanf("%32s", token);
            }
        } else if (strcmp("GET", token) == 0) {
            // Get variable name
            char variable_name[TOKEN_LENGTH];
            scanf("%32s", variable_name);

            // Print variable value
            struct Variable *variable = FindVariable(variable_name, current_scope);
            if (variable != NULL) {
                printf("%s\n", variable->value);
            } else {
                printf("Error: Cannot find variable '%s'\n", variable_name);
            }
        } else if (strcmp("ENTER", token) == 0) {
            // Initialize scope
            struct Scope *scope = malloc(sizeof(struct Scope));
            scope->variables = NULL;
            scope->previous = current_scope;

            current_scope = scope;
        } else if (strcmp("EXIT", token) == 0) {
            if (current_scope == NULL) {
                printf("Error: No scope to exit\n");
                continue;
            }

            // Deallocate variables
            struct Variable *current_variable = current_scope->variables;
            while (current_variable != NULL) {
                free(current_variable->name);
                free(current_variable->value);

                struct Variable *next_variable = current_variable->next;
                free(current_variable);
                current_variable = next_variable;
            }

            // Deallocate scope
            struct Scope *previous_scope = current_scope->previous;
            free(current_scope);
            current_scope = previous_scope;
        } else {
            printf("Error: Command not recognized\n");
        }
    }
    return EXIT_SUCCESS;
}
