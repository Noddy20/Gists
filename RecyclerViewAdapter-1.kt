
/**
 *    onClickItem is passed as function as parameter
 */

class ManageAdvertisementLocalAdapter(var mActivity: Activity, var onClickItem:(model: AdvertisementListModel) -> Unit):
    RecyclerView.Adapter<ManageAdvertisementLocalAdapter.ViewHolder>() {

    // use empty list and update it later from setDataList function 
    // no need to pass list in adapter constructor
    private var arrayModel = emptyList<AdvertisementListModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_advertisement_manage, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //use bind function inside viewholder class to access view directly 
        holder.bind(arrayModel[position])
    }

    override fun getItemCount(): Int {
        return arrayModel.size
    }

    fun setDataList(list: List<AdvertisementListModel>){
        //update the adapter list here no need to clear and addAll
        arrayModel = list
        notifyDataSetChanged()
    }

    fun getItemModel(pos: Int): AdvertisementListModel?{
        kotlin.runCatching {
            return arrayModel[pos]
        }
        return null
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer, View.OnClickListener {

        //LayoutContainer is used to cache view (no need to access viewholder.itemview again and again)
        //LayoutContainer can be enabled by setting experimetal = true in gradle 
        override val containerView: View?
            get() = itemView

        init {
            //Initialize only once when the ViewHolder object is created 
            linItem.setOnClickListener(this)
        }

        fun bind(model: AdvertisementListModel){
            model.run {
                txtAdvName.text = advGivenName?:"-"
                txtAdvEndDate.text = advEndDate?:"-"
                txtAdvStatus.text = advStatus?:"-"

                val bgStatus = statusView.background as GradientDrawable?
                bgStatus?.let {
                    it.setColor(getColorByStatus(advStatus, mActivity))
                }

            }
        }

        override fun onClick(p0: View?) {
            if (p0?.id == R.id.linItem){
                getItemModel(adapterPosition)?.let {
                    //send either adapterPosition or the model itself 
                    onClickItem(it)
                }
            }
        }


    }

    private val newColor = ContextCompat.getColor(mActivity, R.color.colorAdvNew)
    private val runningColor = ContextCompat.getColor(mActivity, R.color.colorAdvRunning)
    private val expiredColor = ContextCompat.getColor(mActivity, R.color.colorAdvExpired)

    private fun getColorByStatus(status: String?, context: Context): Int{
        return when(status){
            "Expired" -> expiredColor
            "New" -> newColor
            "Running" -> runningColor
            else -> ContextCompat.getColor(context, R.color.colorShadowGrey)
        }
    }
}
