// No mock data — data is passed entirely as a prop from DashboardPage

const TYPE_ICON = {
    order: { icon: "fi fi-rr-shopping-cart", bg: "bg-blue-50", color: "text-blue-600" },
    user: { icon: "fi fi-rr-user-add", bg: "bg-emerald-50", color: "text-emerald-600" },
    stock: { icon: "fi fi-rr-triangle-warning", bg: "bg-amber-50", color: "text-amber-600" },
};

function getRelativeTime(isoValue) {
    if (!isoValue) return "Just now";
    const created = new Date(isoValue).getTime();
    if (Number.isNaN(created)) return "Just now";

    const diffSeconds = Math.max(0, Math.floor((Date.now() - created) / 1000));
    if (diffSeconds < 60) return "Just now";
    if (diffSeconds < 3600) return `${Math.floor(diffSeconds / 60)} min ago`;
    if (diffSeconds < 86400) return `${Math.floor(diffSeconds / 3600)} hour${Math.floor(diffSeconds / 3600) > 1 ? "s" : ""} ago`;
    return `${Math.floor(diffSeconds / 86400)} day${Math.floor(diffSeconds / 86400) > 1 ? "s" : ""} ago`;
}

function ActivityFeed({ data = [] }) {
    const list = Array.isArray(data) ? data : [];
    return (
        <div className="bg-white rounded-xl shadow-sm border border-slate-100 p-5">
            <div className="mb-4">
                <h3 className="text-sm font-semibold font-heading text-slate-800">Activity Feed</h3>
                <p className="text-xs text-slate-400 font-body mt-0.5">Recent platform events</p>
            </div>

            <ul className="relative">
                {/* Timeline line */}
                <div className="absolute left-[13px] top-3 bottom-3 w-px bg-slate-100" />

                {list.map((item, i) => {
                    const message = String(item?.message ?? "").toLowerCase();
                    const type = message.includes("registered") ? "user" : message.includes("stock") ? "stock" : "order";
                    const cfg = TYPE_ICON[type] || TYPE_ICON.order;
                    return (
                        <li key={`${item?.message ?? "event"}-${item?.createdAt ?? i}`} className="flex items-start gap-3 pb-4 last:pb-0 relative">
                            <div className={`w-7 h-7 rounded-xl flex items-center justify-center flex-shrink-0 z-10 ${cfg.bg}`}>
                                <i className={`${cfg.icon} text-xs leading-none ${cfg.color}`} />
                            </div>
                            <div className="flex-1 min-w-0 pt-0.5">
                                <p className="text-xs text-slate-700 font-body leading-snug">{item?.message ?? "Logged event"}</p>
                                <p className="text-[10px] text-slate-400 font-body mt-0.5">{getRelativeTime(item?.createdAt)}</p>
                            </div>
                        </li>
                    );
                })}
            </ul>
        </div>
    );
}

export default ActivityFeed;
