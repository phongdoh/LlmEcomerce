import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
} from "recharts";
// No mock data — data is passed entirely as a prop from DashboardPage

const CustomTooltip = ({ active, payload, label }) => {
    if (!active || !payload?.length) return null;
    return (
        <div className="bg-slate-900 rounded-xl p-3 shadow-xl border border-slate-800">
            <p className="text-xs text-slate-400 mb-1 font-body">{label}</p>
            <p className="text-xs font-bold text-emerald-400 font-heading">{Number(payload[0]?.value ?? 0).toLocaleString("vi-VN")} users</p>
        </div>
    );
};

function UserGrowthChart({ data = [] }) {
    const list = Array.isArray(data) ? data : [];
    const totalThisMonth = list.reduce((sum, item) => sum + Number(item?.count ?? 0), 0);

    return (
        <div className="bg-white rounded-xl shadow-sm border border-slate-100 p-5">
            <div className="flex items-center justify-between mb-2">
                <div>
                    <h3 className="text-sm font-semibold font-heading text-slate-800">User Growth</h3>
                    <p className="text-xs text-slate-400 font-body mt-0.5">New users per day (current month)</p>
                </div>
                <div className="text-right">
                    <p className="text-lg font-bold font-heading text-slate-800">
                        {totalThisMonth.toLocaleString("vi-VN")}
                    </p>
                    <p className="text-xs font-medium font-body text-emerald-600">
                        Current month total
                    </p>
                </div>
            </div>
            {list.length === 0 ? (
                <div className="h-[180px] flex items-center justify-center text-slate-400 text-xs font-body">
                    No user growth data available.
                </div>
            ) : (
                <ResponsiveContainer width="100%" height={180}>
                    <LineChart data={list} margin={{ top: 8, right: 4, left: -20, bottom: 0 }}>
                        <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" vertical={false} />
                        <XAxis dataKey="date" tick={{ fontSize: 11, fill: "#94a3b8" }} axisLine={false} tickLine={false} />
                        <YAxis tick={{ fontSize: 11, fill: "#94a3b8" }} axisLine={false} tickLine={false} />
                        <Tooltip content={<CustomTooltip />} cursor={{ stroke: "#e2e8f0", strokeWidth: 1 }} />
                        <Line type="monotone" dataKey="count" name="New Users" stroke="#10b981" strokeWidth={2.5} dot={{ fill: "#10b981", strokeWidth: 0, r: 3 }} activeDot={{ r: 5 }} />
                    </LineChart>
                </ResponsiveContainer>
            )}
        </div>
    );
}

export default UserGrowthChart;
