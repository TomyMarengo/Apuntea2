import {renderHook} from "@testing-library/react";
import {Provider} from "react-redux";

export function useWithWrapper(hook, store) {
    return renderHook(hook, {
        wrapper: ({ children }) => <Provider store={store}>{children}</Provider>,
    });
}