import { Store } from 'vuex';

// Define the state type
interface AuthState {
    token: string | null;
}

// Define the getters type
interface AuthGetters {
    isAuthenticated: boolean;
}

// Define the mutations type
interface AuthMutations {
    setToken(state: AuthState, token: string): void;
    clearToken(state: AuthState): void;
}

// Define the actions type
interface AuthActions {
    login({ commit }: { commit: any }, payload: any): Promise<any>;
    register({ commit }: { commit: any }, payload: any): Promise<any>;
    logout({ commit }: { commit: any }): void;
}

// Define the type for the whole store
export type AuthStore = Store<AuthState> & AuthGetters & AuthMutations & AuthActions;
