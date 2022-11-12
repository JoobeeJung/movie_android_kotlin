package com.kbstar.movieproject.swifemenu.step

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import android.widget.Toast
import com.kbstar.movieproject.MyApplication
import com.kbstar.movieproject.R
import com.kbstar.movieproject.dto.Booking
import com.kbstar.movieproject.recyclerview.BookingAdapter
import retrofit2.Call
import retrofit2.Response


class SwipeDeleteCallback(
    val context: Context,
    val recyclerView: RecyclerView,
    val datas: MutableList<Booking>
) : ItemTouchHelper.Callback() {

    var adapter: BookingAdapter

    init {
        adapter = (recyclerView.adapter as BookingAdapter)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    //swipe 가 끝난후 한번 호출
    //swipe 진행되다가 원 상태로 되돌아 가면 호출되지 않는다.
    //끝까지 진행된 경우 호출
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //swipe 가 발생한 항목의 index 및 항목의 데이터 획득
        val deletedItem = datas.get(viewHolder.adapterPosition)
        val deletedIndex = viewHolder.adapterPosition

        if (direction == ItemTouchHelper.LEFT) {
            //왼쪽 이벤트....
            adapter.removeItem(deletedIndex)
            val snackbar = Snackbar.make(
                recyclerView,
                "${deletedItem.movie_name} removed from your history!", Snackbar.LENGTH_LONG
            )
            snackbar.setAction("UNDO", object : View.OnClickListener {
                override fun onClick(view: View?) {

                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedIndex, deletedItem)
                }
            })
            snackbar.show()

            snackbar.addCallback(object: Snackbar.Callback() {
                override fun onShown(sb: Snackbar?) {
                    super.onShown(sb)
                    Log.d("MainActivity", "shown!")
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    Log.d("MainActivity", "dismissed!")
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        // Snackbar closed on its own

                        val bookingService = (context.applicationContext as MyApplication).bookingService
                        val call = bookingService.cancelBooking(deletedItem.booking_idx, deletedItem.user_id)

                        call.enqueue(object: retrofit2.Callback<String>{
                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {
                                val result = response.body() ?: ""
                                Log.d("jbjung cancel =====> ",result.toString())
                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(
                                call: Call<String>,
                                t: Throwable
                            ) {
                                t.printStackTrace()
                                call.cancel()
                            }
                        })
                    }
                }

            });


        }else if (direction == ItemTouchHelper.RIGHT) {

            adapter.updateItem(deletedIndex, deletedItem)
            adapter.notifyItemChanged(viewHolder.adapterPosition);
//            val snackbar = Snackbar.make(
//                recyclerView,
//                "$deletedItem save from list!..", Snackbar.LENGTH_LONG
//            )
//            snackbar.setAction("UNDO", object : View.OnClickListener {
//                override fun onClick(view: View?) {
//
//                    // undo is selected, restore the deleted item
//                    adapter.restoreItem(deletedIndex, deletedItem)
//                }
//            })
//            snackbar.show()
        }

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }


    //swipe 순간 빈번하게 호출..
    //actionState - Int: The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
    //isCurrentlyActive - Boolean: True if this view is currently being controlled by the user or false it is simply animating back to its original state.

    override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                             dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val viewItem = viewHolder.itemView
            if(dX < 0) {
                SwipeBackgroundHelper.paintDrawCommandToStart(
                    canvas,
                    viewItem,
                    R.drawable.ic_delete_black_24dp,
                    R.color.red,
                    dX
                )
            }else if(dX > 0) {
                SwipeBackgroundHelper.paintDrawCommandToStart(
                    canvas,
                    viewItem,
                    R.drawable.ic_baseline_edit_24,
                    R.color.green,
                    dX
                )
            }
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}

class SwipeBackgroundHelper {

    companion object {

        private const val THRESHOLD = 2.5

        private const val OFFSET_PX = 20

        @JvmStatic
        fun paintDrawCommandToStart(canvas: Canvas, viewItem: View, @DrawableRes iconResId: Int, backgroundColor: Int, dX: Float) {
            val drawCommand = createDrawCommand(viewItem, dX, iconResId, backgroundColor)
            paintDrawCommand(drawCommand, canvas, dX, viewItem)
        }

        private fun createDrawCommand(viewItem: View, dX: Float, iconResId: Int, backgroundColor: Int): DrawCommand {
            val context = viewItem.context
            var icon = ContextCompat.getDrawable(context, iconResId)
            icon = DrawableCompat.wrap(icon!!).mutate()
            icon.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.white),
                PorterDuff.Mode.SRC_IN)
            val backgroundColor = getBackgroundColor(backgroundColor, R.color.gray, dX, viewItem)
            return DrawCommand(icon, backgroundColor)
        }

        private fun getBackgroundColor(firstColor: Int, secondColor: Int, dX: Float, viewItem: View): Int {
            return when (willActionBeTriggered(dX, viewItem.width)) {
                true -> ContextCompat.getColor(viewItem.context, firstColor)
                false -> ContextCompat.getColor(viewItem.context, secondColor)
            }
        }

        private fun paintDrawCommand(drawCommand: DrawCommand, canvas: Canvas, dX: Float, viewItem: View) {
            drawBackground(canvas, viewItem, dX, drawCommand.backgroundColor)
            drawIcon(canvas, viewItem, dX, drawCommand.icon)
        }

        private fun drawIcon(canvas: Canvas, viewItem: View, dX: Float, icon: Drawable) {
            val topMargin = calculateTopMargin(icon, viewItem)
            icon.bounds = getStartContainerRectangle(viewItem, icon.intrinsicWidth, topMargin, OFFSET_PX, dX)
            icon.draw(canvas)
        }

        private fun getStartContainerRectangle(viewItem: View, iconWidth: Int, topMargin: Int, sideOffset: Int,
                                               dx: Float): Rect {

            Log.d("kkang", "dx..: $dx")
            if(dx<0){
                val leftBound = viewItem.right + dx.toInt() + sideOffset
                val rightBound = viewItem.right + dx.toInt() + iconWidth + sideOffset
                val topBound = viewItem.top + topMargin
                val bottomBound = viewItem.bottom - topMargin

                return Rect(leftBound, topBound, rightBound, bottomBound)
            }else{
                val leftBound =  dx.toInt() - iconWidth - sideOffset
                val rightBound =  dx.toInt() - sideOffset
                val topBound = viewItem.top + topMargin
                val bottomBound = viewItem.bottom - topMargin
                Log.d("kkang","leftBound:$leftBound, rightBound:$rightBound")
                return Rect(leftBound, topBound, rightBound, bottomBound)
            }

        }

        private fun calculateTopMargin(icon: Drawable, viewItem: View): Int {
            return (viewItem.height - icon.intrinsicHeight) / 2
        }

        private fun drawBackground(canvas: Canvas, viewItem: View, dX: Float, color: Int) {
            val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            backgroundPaint.color = color
            val backgroundRectangle = getBackGroundRectangle(viewItem, dX)
            Log.d("kkang",".....${backgroundRectangle.left},${backgroundRectangle.right}")
            canvas.drawRect(backgroundRectangle, backgroundPaint)
        }

        //백그라운드를 그릴 사각형 정보 계산
        private fun getBackGroundRectangle(viewItem: View, dX: Float): RectF {
            if(dX<0) {
                return RectF(
                    viewItem.right.toFloat() + dX, viewItem.top.toFloat(), viewItem.right.toFloat(),
                    viewItem.bottom.toFloat()
                )
            }else {
                return RectF(
                    0f , viewItem.top.toFloat(), dX,
                    viewItem.bottom.toFloat()
                )
            }
        }

        private fun willActionBeTriggered(dX: Float, viewWidth: Int): Boolean {
            return Math.abs(dX) >= viewWidth / THRESHOLD
        }
    }

    private class DrawCommand internal constructor(internal val icon: Drawable, internal val backgroundColor: Int)

}

