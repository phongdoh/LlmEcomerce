import { Link } from "react-router-dom";
// No mock data — data is passed entirely as a prop from DashboardPage

function TopProducts({ data = [] }) {
    const list = Array.isArray(data) ? data : [];
    return (
        <div className="bg-white rounded-xl shadow-sm border border-slate-100 p-5">
            <div className="flex items-center justify-between mb-4">
                <div>
                    <h3 className="text-sm font-semibold font-heading text-slate-800">Top Products</h3>
                    <p className="text-xs text-slate-400 font-body mt-0.5">By sales volume this period</p>
                </div>
                <Link to="/admin/product-manage" className="text-xs text-brand-600 hover:text-brand-700 font-medium font-body transition-colors duration-150">
                    View all →
                </Link>
            </div>

            {list.length === 0 ? (
                <p className="text-xs text-slate-400 font-body">No data yet</p>
            ) : (
                <ul className="space-y-3">
                    {list.map((item, i) => (
                        <li key={item?.productId ?? i} className="flex items-center gap-3 group">
                            <span className={`flex-shrink-0 w-6 text-xs font-bold font-heading text-right ${i === 0 ? "text-amber-500" : i === 1 ? "text-slate-400" : i === 2 ? "text-amber-700" : "text-slate-300"}`}>
                                #{i + 1}
                            </span>

                            <div className="flex-1 min-w-0">
                                <p className="text-xs font-semibold text-slate-700 truncate font-body group-hover:text-brand-600 transition-colors duration-150">
                                    {item?.productName ?? "Unknown Product"}
                                </p>
                                <p className="text-[10px] text-slate-400 font-body">Sales volume</p>
                            </div>

                            <div className="text-right flex-shrink-0">
                                <p className="text-xs font-bold font-heading text-slate-700">
                                    {Number(item?.salesVolume ?? 0).toLocaleString("vi-VN")}
                                </p>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default TopProducts;
