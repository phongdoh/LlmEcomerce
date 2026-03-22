import APIBase from "../api/ApiBase";

const EMPTY_RESULTS = {
    products: [],
    orders: [],
    users: [],
};

export async function searchAdminGlobal({ query, limit = 5 } = {}) {
    const safeQuery = String(query ?? "").trim();
    if (!safeQuery) {
        return EMPTY_RESULTS;
    }

    try {
        const { data } = await APIBase.get("/api/v1/admin/search", {
            params: {
                query: safeQuery,
                limit,
            },
        });

        return {
            products: Array.isArray(data?.products) ? data.products : [],
            orders: Array.isArray(data?.orders) ? data.orders : [],
            users: Array.isArray(data?.users) ? data.users : [],
        };
    } catch (err) {
        console.error("[adminSearchService] searchAdminGlobal failed:", err);
        return EMPTY_RESULTS;
    }
}
