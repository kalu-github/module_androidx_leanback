/*    1:     */ 
/*    2:     */ 
/*    3:     */ android.annotation.SuppressLint
/*    4:     */ android.content.Context
/*    5:     */ android.content.res.TypedArray
/*    6:     */ android.graphics.Rect
/*    7:     */ android.util.AttributeSet
/*    8:     */ android.view.KeyEvent
/*    9:     */ android.view.MotionEvent
/*   10:     */ android.view.View
/*   11:     */ android.view.animation.Interpolator
/*   12:     */ androidx.annotation.NonNull
/*   13:     */ androidx.annotation.Nullable
/*   14:     */ androidx.annotation.RestrictTo
/*   15:     */ androidx.recyclerview.widget.RecyclerView
/*   16:     */ androidx.recyclerview.widget.RecyclerView.ItemAnimator
/*   17:     */ androidx.recyclerview.widget.RecyclerView.LayoutManager
/*   18:     */ androidx.recyclerview.widget.RecyclerView.RecyclerListener
/*   19:     */ androidx.recyclerview.widget.RecyclerView.State
/*   20:     */ androidx.recyclerview.widget.RecyclerView.ViewHolder
/*   21:     */ androidx.recyclerview.widget.SimpleItemAnimator
/*   22:     */ 
/*   23:     */ 
/*   24:     */ 
/*   25:     */ 
/*   26:     */ 
/*   27:     */ 
/*   28:     */ 
/*   29:     */ 
/*   30:     */ 
/*   31:     */ 
/*   32:     */ 
/*   33:     */ 
/*   34:     */ 
/*   35:     */ 
/*   36:     */ 
/*   37:     */ 
/*   38:     */ 
/*   39:     */ 
/*   40:     */ 
/*   41:     */ 
/*   42:     */ 
/*   43:     */ 
/*   44:     */ 
/*   45:     */ 
/*   46:     */ 
/*   47:     */ 
/*   48:     */ 
/*   49:     */ 
/*   50:     */ 
/*   51:     */ 
/*   52:     */ 
/*   53:     */ 
/*   54:     */ 
/*   55:     */ 
/*   56:     */ 
/*   57:     */ 
/*   58:     */ 
/*   59:     */ 
/*   60:     */ 
/*   61:     */ 
/*   62:     */ 
/*   63:     */ 
/*   64:     */ 
/*   65:     */ 
/*   66:     */ 
/*   67:     */ 
/*   68:     */ 
/*   69:     */ 
/*   70:     */ 
/*   71:     */ 
/*   72:     */ 
/*   73:     */ 
/*   74:     */ 
/*   75:     */ 
/*   76:     */ 
/*   77:     */ 
/*   78:     */ 
/*   79:     */ 
/*   80:     */ 
/*   81:     */ 
/*   82:     */ 
/*   83:     */ 
/*   84:     */ 
/*   85:     */ 
/*   86:     */ 
/*   87:     */ 
/*   88:     */ 
/*   89:     */ 
/*   90:     */ 
/*   91:     */ 
/*   92:     */ 
/*   93:     */ 
/*   94:     */ 
/*   95:     */ 
/*   96:     */ 
/*   97:     */ 
/*   98:     */ 
/*   99:     */ 
/*  100:     */ 
/*  101:     */ 
/*  102:     */ 
/*  103:     */ 
/*  104:     */ 
/*  105:     */ 
/*  106:     */ 
/*  107:     */ 
/*  108:     */ 
/*  109:     */ 
/*  110:     */ 
/*  111:     */ 
/*  112:     */ 
/*  113:     */ 
/*  114:     */ 
/*  115:     */ 
/*  116:     */ 
/*  117:     */ 
/*  118:     */ 
/*  119:     */ 
/*  120:     */ 
/*  121:     */ 
/*  122:     */ 
/*  123:     */ 
/*  124:     */ 
/*  125:     */ 
/*  126:     */ 
/*  127:     */ 
/*  128:     */ 
/*  129:     */ 
/*  130:     */ 
/*  131:     */ 
/*  132:     */ 
/*  133:     */ 
/*  134:     */ 
/*  135:     */ 
/*  136:     */ 
/*  137:     */ 
/*  138:     */ 
/*  139:     */ 
/*  140:     */ 
/*  141:     */ 
/*  142:     */ 
/*  143:     */ 
/*  144:     */ 
/*  145:     */ 
/*  146:     */ 
/*  147:     */ 
/*  148:     */ 
/*  149:     */ 
/*  150:     */ 
/*  151:     */ 
/*  152:     */ 
/*  153:     */ 
/*  154:     */ 
/*  155:     */ 
/*  156:     */ 
/*  157:     */ 
/*  158:     */ 
/*  159:     */ 
/*  160:     */ 
/*  161:     */ 
/*  162:     */ 
/*  163:     */ 
/*  164:     */ 
/*  165:     */ 
/*  166:     */ 
/*  167:     */ 
/*  168:     */ 
/*  169:     */ 
/*  170:     */ 
/*  171:     */ 
/*  172:     */ 
/*  173:     */ 
/*  174:     */ 
/*  175:     */ 
/*  176:     */ 
/*  177:     */ 
/*  178:     */ 
/*  179:     */ 
/*  180:     */ 
/*  181:     */ 
/*  182:     */ 
/*  183:     */ 
/*  184:     */ 
/*  185:     */ 
/*  186:     */ 
/*  187:     */ 
/*  188:     */ 
/*  189:     */ 
/*  190:     */ 
/*  191:     */ 
/*  192:     */ 
/*  193:     */ 
/*  194:     */ 
/*  195:     */ 
/*  196:     */ 
/*  197:     */ 
/*  198:     */ 
/*  199:     */ 
/*  200:     */ 
/*  201:     */ 
/*  202:     */ 
/*  203:     */ 
/*  204:     */ 
/*  205:     */ 
/*  206:     */ 
/*  207:     */ 
/*  208:     */ 
/*  209:     */ 
/*  210:     */ 
/*  211:     */ 
/*  212:     */ 
/*  213:     */ 
/*  214:     */ 
/*  215:     */ 
/*  216:     */ 
/*  217:     */ 
/*  218:     */ 
/*  219:     */ 
/*  220:     */ 
/*  221:     */ 
/*  222:     */ 
/*  223:     */ 
/*  224:     */ 
/*  225:     */ 
/*  226:     */ 
/*  227:     */ BaseGridView
/*  228:     */   
/*  229:     */ 
/*  230:     */   LIBRARY_GROUP_PREFIX
/*  231:     */   FOCUS_SCROLL_ALIGNED = 0
/*  232:     */   LIBRARY_GROUP_PREFIX
/*  233:     */   FOCUS_SCROLL_ITEM = 1
/*  234:     */   LIBRARY_GROUP_PREFIX
/*  235:     */   FOCUS_SCROLL_PAGE = 2
/*  236:     */   WINDOW_ALIGN_LOW_EDGE = 1
/*  237:     */   WINDOW_ALIGN_HIGH_EDGE = 2
/*  238:     */   WINDOW_ALIGN_BOTH_EDGE = 3
/*  239:     */   WINDOW_ALIGN_NO_EDGE = 0
/*  240:     */   WINDOW_ALIGN_OFFSET_PERCENT_DISABLED = -1.0F
/*  241:     */   ITEM_ALIGN_OFFSET_PERCENT_DISABLED = -1.0F
/*  242:     */   SAVE_NO_CHILD = 0
/*  243:     */   SAVE_ON_SCREEN_CHILD = 1
/*  244:     */   SAVE_LIMITED_CHILD = 2
/*  245:     */   SAVE_ALL_CHILD = 3
/*  246:     */   PFLAG_RETAIN_FOCUS_FOR_CHILD = 1
/*  247:     */   mLayoutManager
/*  248:     */   mSmoothScrollByBehavior
/*  249: 249 */   mAnimateChildLayout = 
/*  250:     */   
/*  251: 251 */   mHasOverlappingRendering = 
/*  252:     */   
/*  253:     */   mSavedItemAnimator
/*  254:     */   
/*  255:     */   mOnTouchInterceptListener
/*  256:     */   
/*  257:     */   mOnMotionInterceptListener
/*  258:     */   
/*  259:     */   mOnKeyInterceptListener
/*  260:     */   
/*  261:     */   mOnUnhandledKeyListener
/*  262:     */   
/*  263: 263 */   mInitialPrefetchItemCount = 4
/*  264:     */   mPrivateFlag
/*  265:     */   
/*  266:     */   BaseGridView, , 
/*  267:     */   
/*  268: 268 */     , , 
/*  269: 269 */     mLayoutManager = new GridLayoutManager(this);
/*  270: 270 */     setLayoutManager(mLayoutManager);
/*  271:     */     
/*  272: 272 */     setPreserveFocusAfterLayout(false);
/*  273: 273 */     setDescendantFocusability(262144);
/*  274: 274 */     setHasFixedSize(true);
/*  275: 275 */     setChildrenDrawingOrderEnabled(true);
/*  276: 276 */     setWillNotDraw(true);
/*  277: 277 */     setOverScrollMode(2);
/*  278:     */     
/*  279:     */ 
/*  280:     */ 
/*  281: 281 */     ((SimpleItemAnimator)getItemAnimator()).setSupportsChangeAnimations(false);
/*  282: 282 */     super.addRecyclerListener(new RecyclerView.RecyclerListener()
/*  283:     */     {
/*  284:     */       public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
/*  285: 285 */         mLayoutManager.onChildRecycled(holder);
/*  286:     */       }
/*  287:     */     });
/*  288:     */   }
/*  289:     */   
/*  290:     */   @SuppressLint({"CustomViewStyleable"})
/*  291:     */   void initBaseGridViewAttributes(Context context, AttributeSet attrs) {
/*  292: 292 */     TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lbBaseGridView);
/*  293: 293 */     boolean throughFront = a.getBoolean(R.styleable.lbBaseGridView_focusOutFront, false);
/*  294: 294 */     boolean throughEnd = a.getBoolean(R.styleable.lbBaseGridView_focusOutEnd, false);
/*  295: 295 */     mLayoutManager.setFocusOutAllowed(throughFront, throughEnd);
/*  296: 296 */     boolean throughSideStart = a.getBoolean(R.styleable.lbBaseGridView_focusOutSideStart, true);
/*  297: 297 */     boolean throughSideEnd = a.getBoolean(R.styleable.lbBaseGridView_focusOutSideEnd, true);
/*  298: 298 */     mLayoutManager.setFocusOutSideAllowed(throughSideStart, throughSideEnd);
/*  299: 299 */     mLayoutManager.setVerticalSpacing(a
/*  300: 300 */       .getDimensionPixelSize(R.styleable.lbBaseGridView_android_verticalSpacing, a
/*  301: 301 */       .getDimensionPixelSize(R.styleable.lbBaseGridView_verticalMargin, 0)));
/*  302: 302 */     mLayoutManager.setHorizontalSpacing(a
/*  303: 303 */       .getDimensionPixelSize(R.styleable.lbBaseGridView_android_horizontalSpacing, a
/*  304: 304 */       .getDimensionPixelSize(R.styleable.lbBaseGridView_horizontalMargin, 0)));
/*  305: 305 */     if (a.hasValue(R.styleable.lbBaseGridView_android_gravity)) {
/*  306: 306 */       setGravity(a.getInt(R.styleable.lbBaseGridView_android_gravity, 0));
/*  307:     */     }
/*  308: 308 */     a.recycle();
/*  309:     */   }
/*  310:     */   
/*  311:     */ 
/*  312:     */ 
/*  313:     */ 
/*  314:     */ 
/*  315:     */ 
/*  316:     */ 
/*  317:     */ 
/*  318:     */ 
/*  319:     */   @RestrictTo({androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/*  320:     */   public void setFocusScrollStrategy(int scrollStrategy)
/*  321:     */   {
/*  322: 322 */     if ((scrollStrategy != 0) && (scrollStrategy != 1) && (scrollStrategy != 2))
/*  323:     */     {
/*  324: 324 */       throw new IllegalArgumentException("Invalid scrollStrategy");
/*  325:     */     }
/*  326: 326 */     mLayoutManager.setFocusScrollStrategy(scrollStrategy);
/*  327: 327 */     requestLayout();
/*  328:     */   }
/*  329:     */   
/*  330:     */ 
/*  331:     */ 
/*  332:     */ 
/*  333:     */ 
/*  334:     */ 
/*  335:     */ 
/*  336:     */ 
/*  337:     */ 
/*  338:     */   @RestrictTo({androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/*  339:     */   public int getFocusScrollStrategy()
/*  340:     */   {
/*  341: 341 */     return mLayoutManager.getFocusScrollStrategy();
/*  342:     */   }
/*  343:     */   
/*  344:     */ 
/*  345:     */ 
/*  346:     */ 
/*  347:     */ 
/*  348:     */ 
/*  349:     */ 
/*  350:     */   public void setWindowAlignment(int windowAlignment)
/*  351:     */   {
/*  352: 352 */     mLayoutManager.setWindowAlignment(windowAlignment);
/*  353: 353 */     requestLayout();
/*  354:     */   }
/*  355:     */   
/*  356:     */ 
/*  357:     */ 
/*  358:     */ 
/*  359:     */ 
/*  360:     */ 
/*  361:     */   public int getWindowAlignment()
/*  362:     */   {
/*  363: 363 */     return mLayoutManager.getWindowAlignment();
/*  364:     */   }
/*  365:     */   
/*  366:     */ 
/*  367:     */ 
/*  368:     */ 
/*  369:     */ 
/*  370:     */ 
/*  371:     */ 
/*  372:     */ 
/*  373:     */ 
/*  374:     */   public void setWindowAlignmentPreferKeyLineOverLowEdge(boolean preferKeyLineOverLowEdge)
/*  375:     */   {
/*  376: 376 */     mLayoutManager.mWindowAlignment.mainAxis().setPreferKeylineOverLowEdge(preferKeyLineOverLowEdge);
/*  377: 377 */     requestLayout();
/*  378:     */   }
/*  379:     */   
/*  380:     */ 
/*  381:     */ 
/*  382:     */ 
/*  383:     */ 
/*  384:     */ 
/*  385:     */ 
/*  386:     */ 
/*  387:     */ 
/*  388:     */ 
/*  389:     */   public void setWindowAlignmentPreferKeyLineOverHighEdge(boolean preferKeyLineOverHighEdge)
/*  390:     */   {
/*  391: 391 */     mLayoutManager.mWindowAlignment.mainAxis().setPreferKeylineOverHighEdge(preferKeyLineOverHighEdge);
/*  392: 392 */     requestLayout();
/*  393:     */   }
/*  394:     */   
/*  395:     */ 
/*  396:     */ 
/*  397:     */ 
/*  398:     */ 
/*  399:     */ 
/*  400:     */ 
/*  401:     */ 
/*  402:     */   public boolean isWindowAlignmentPreferKeyLineOverLowEdge()
/*  403:     */   {
/*  404: 404 */     return mLayoutManager.mWindowAlignment.mainAxis().isPreferKeylineOverLowEdge();
/*  405:     */   }
/*  406:     */   
/*  407:     */ 
/*  408:     */ 
/*  409:     */ 
/*  410:     */ 
/*  411:     */ 
/*  412:     */ 
/*  413:     */ 
/*  414:     */ 
/*  415:     */   public boolean isWindowAlignmentPreferKeyLineOverHighEdge()
/*  416:     */   {
/*  417: 417 */     return mLayoutManager.mWindowAlignment.mainAxis().isPreferKeylineOverHighEdge();
/*  418:     */   }
/*  419:     */   
/*  420:     */ 
/*  421:     */ 
/*  422:     */ 
/*  423:     */ 
/*  424:     */ 
/*  425:     */ 
/*  426:     */ 
/*  427:     */ 
/*  428:     */ 
/*  429:     */   public void setWindowAlignmentOffset(int offset)
/*  430:     */   {
/*  431: 431 */     mLayoutManager.setWindowAlignmentOffset(offset);
/*  432: 432 */     requestLayout();
/*  433:     */   }
/*  434:     */   
/*  435:     */ 
/*  436:     */ 
/*  437:     */ 
/*  438:     */ 
/*  439:     */ 
/*  440:     */ 
/*  441:     */ 
/*  442:     */ 
/*  443:     */   public int getWindowAlignmentOffset()
/*  444:     */   {
/*  445: 445 */     return mLayoutManager.getWindowAlignmentOffset();
/*  446:     */   }
/*  447:     */   
/*  448:     */ 
/*  449:     */ 
/*  450:     */ 
/*  451:     */ 
/*  452:     */ 
/*  453:     */ 
/*  454:     */ 
/*  455:     */ 
/*  456:     */   public void setWindowAlignmentOffsetPercent(float offsetPercent)
/*  457:     */   {
/*  458: 458 */     mLayoutManager.setWindowAlignmentOffsetPercent(offsetPercent);
/*  459: 459 */     requestLayout();
/*  460:     */   }
/*  461:     */   
/*  462:     */ 
/*  463:     */ 
/*  464:     */ 
/*  465:     */ 
/*  466:     */ 
/*  467:     */ 
/*  468:     */ 
/*  469:     */   public float getWindowAlignmentOffsetPercent()
/*  470:     */   {
/*  471: 471 */     return mLayoutManager.getWindowAlignmentOffsetPercent();
/*  472:     */   }
/*  473:     */   
/*  474:     */ 
/*  475:     */ 
/*  476:     */ 
/*  477:     */ 
/*  478:     */ 
/*  479:     */ 
/*  480:     */ 
/*  481:     */   public void setItemAlignmentOffset(int offset)
/*  482:     */   {
/*  483: 483 */     mLayoutManager.setItemAlignmentOffset(offset);
/*  484: 484 */     requestLayout();
/*  485:     */   }
/*  486:     */   
/*  487:     */ 
/*  488:     */ 
/*  489:     */ 
/*  490:     */ 
/*  491:     */ 
/*  492:     */ 
/*  493:     */ 
/*  494:     */ 
/*  495:     */   public int getItemAlignmentOffset()
/*  496:     */   {
/*  497: 497 */     return mLayoutManager.getItemAlignmentOffset();
/*  498:     */   }
/*  499:     */   
/*  500:     */ 
/*  501:     */ 
/*  502:     */ 
/*  503:     */ 
/*  504:     */ 
/*  505:     */ 
/*  506:     */ 
/*  507:     */ 
/*  508:     */ 
/*  509:     */   public void setItemAlignmentOffsetWithPadding(boolean withPadding)
/*  510:     */   {
/*  511: 511 */     mLayoutManager.setItemAlignmentOffsetWithPadding(withPadding);
/*  512: 512 */     requestLayout();
/*  513:     */   }
/*  514:     */   
/*  515:     */ 
/*  516:     */ 
/*  517:     */ 
/*  518:     */ 
/*  519:     */ 
/*  520:     */ 
/*  521:     */ 
/*  522:     */ 
/*  523:     */ 
/*  524:     */   public boolean isItemAlignmentOffsetWithPadding()
/*  525:     */   {
/*  526: 526 */     return mLayoutManager.isItemAlignmentOffsetWithPadding();
/*  527:     */   }
/*  528:     */   
/*  529:     */ 
/*  530:     */ 
/*  531:     */ 
/*  532:     */ 
/*  533:     */ 
/*  534:     */ 
/*  535:     */ 
/*  536:     */ 
/*  537:     */ 
/*  538:     */   public void setItemAlignmentOffsetPercent(float offsetPercent)
/*  539:     */   {
/*  540: 540 */     mLayoutManager.setItemAlignmentOffsetPercent(offsetPercent);
/*  541: 541 */     requestLayout();
/*  542:     */   }
/*  543:     */   
/*  544:     */ 
/*  545:     */ 
/*  546:     */ 
/*  547:     */ 
/*  548:     */ 
/*  549:     */ 
/*  550:     */ 
/*  551:     */   public float getItemAlignmentOffsetPercent()
/*  552:     */   {
/*  553: 553 */     return mLayoutManager.getItemAlignmentOffsetPercent();
/*  554:     */   }
/*  555:     */   
/*  556:     */ 
/*  557:     */ 
/*  558:     */ 
/*  559:     */ 
/*  560:     */ 
/*  561:     */   public void setItemAlignmentViewId(int viewId)
/*  562:     */   {
/*  563: 563 */     mLayoutManager.setItemAlignmentViewId(viewId);
/*  564:     */   }
/*  565:     */   
/*  566:     */ 
/*  567:     */ 
/*  568:     */ 
/*  569:     */ 
/*  570:     */ 
/*  571:     */ 
/*  572:     */   public int getItemAlignmentViewId()
/*  573:     */   {
/*  574: 574 */     return mLayoutManager.getItemAlignmentViewId();
/*  575:     */   }
/*  576:     */   
/*  577:     */ 
/*  578:     */ 
/*  579:     */ 
/*  580:     */ 
/*  581:     */   @Deprecated
/*  582:     */   public void setItemMargin(int margin)
/*  583:     */   {
/*  584: 584 */     setItemSpacing(margin);
/*  585:     */   }
/*  586:     */   
/*  587:     */ 
/*  588:     */ 
/*  589:     */ 
/*  590:     */ 
/*  591:     */   public void setItemSpacing(int spacing)
/*  592:     */   {
/*  593: 593 */     mLayoutManager.setItemSpacing(spacing);
/*  594: 594 */     requestLayout();
/*  595:     */   }
/*  596:     */   
/*  597:     */ 
/*  598:     */ 
/*  599:     */ 
/*  600:     */ 
/*  601:     */   @Deprecated
/*  602:     */   public void setVerticalMargin(int margin)
/*  603:     */   {
/*  604: 604 */     setVerticalSpacing(margin);
/*  605:     */   }
/*  606:     */   
/*  607:     */ 
/*  608:     */ 
/*  609:     */ 
/*  610:     */ 
/*  611:     */   @Deprecated
/*  612:     */   public int getVerticalMargin()
/*  613:     */   {
/*  614: 614 */     return mLayoutManager.getVerticalSpacing();
/*  615:     */   }
/*  616:     */   
/*  617:     */ 
/*  618:     */ 
/*  619:     */ 
/*  620:     */ 
/*  621:     */   @Deprecated
/*  622:     */   public void setHorizontalMargin(int margin)
/*  623:     */   {
/*  624: 624 */     setHorizontalSpacing(margin);
/*  625:     */   }
/*  626:     */   
/*  627:     */ 
/*  628:     */ 
/*  629:     */ 
/*  630:     */ 
/*  631:     */   @Deprecated
/*  632:     */   public int getHorizontalMargin()
/*  633:     */   {
/*  634: 634 */     return mLayoutManager.getHorizontalSpacing();
/*  635:     */   }
/*  636:     */   
/*  637:     */ 
/*  638:     */ 
/*  639:     */ 
/*  640:     */ 
/*  641:     */   public void setVerticalSpacing(int spacing)
/*  642:     */   {
/*  643: 643 */     mLayoutManager.setVerticalSpacing(spacing);
/*  644: 644 */     requestLayout();
/*  645:     */   }
/*  646:     */   
/*  647:     */ 
/*  648:     */ 
/*  649:     */ 
/*  650:     */ 
/*  651:     */   public int getVerticalSpacing()
/*  652:     */   {
/*  653: 653 */     return mLayoutManager.getVerticalSpacing();
/*  654:     */   }
/*  655:     */   
/*  656:     */ 
/*  657:     */ 
/*  658:     */ 
/*  659:     */ 
/*  660:     */   public void setHorizontalSpacing(int spacing)
/*  661:     */   {
/*  662: 662 */     mLayoutManager.setHorizontalSpacing(spacing);
/*  663: 663 */     requestLayout();
/*  664:     */   }
/*  665:     */   
/*  666:     */ 
/*  667:     */ 
/*  668:     */ 
/*  669:     */ 
/*  670:     */   public int getHorizontalSpacing()
/*  671:     */   {
/*  672: 672 */     return mLayoutManager.getHorizontalSpacing();
/*  673:     */   }
/*  674:     */   
/*  675:     */ 
/*  676:     */ 
/*  677:     */ 
/*  678:     */ 
/*  679:     */ 
/*  680:     */   public void setOnChildLaidOutListener(@Nullable OnChildLaidOutListener listener)
/*  681:     */   {
/*  682: 682 */     mLayoutManager.setOnChildLaidOutListener(listener);
/*  683:     */   }
/*  684:     */   
/*  685:     */ 
/*  686:     */ 
/*  687:     */ 
/*  688:     */ 
/*  689:     */ 
/*  690:     */ 
/*  691:     */ 
/*  692:     */ 
/*  693:     */   @SuppressLint({"ReferencesDeprecated"})
/*  694:     */   public void setOnChildSelectedListener(@Nullable OnChildSelectedListener listener)
/*  695:     */   {
/*  696: 696 */     mLayoutManager.setOnChildSelectedListener(listener);
/*  697:     */   }
/*  698:     */   
/*  699:     */ 
/*  700:     */ 
/*  701:     */ 
/*  702:     */ 
/*  703:     */   public final void addOnLayoutCompletedListener(@NonNull OnLayoutCompletedListener listener)
/*  704:     */   {
/*  705: 705 */     mLayoutManager.addOnLayoutCompletedListener(listener);
/*  706:     */   }
/*  707:     */   
/*  708:     */ 
/*  709:     */ 
/*  710:     */ 
/*  711:     */ 
/*  712:     */   public final void removeOnLayoutCompletedListener(@NonNull OnLayoutCompletedListener listener)
/*  713:     */   {
/*  714: 714 */     mLayoutManager.removeOnLayoutCompletedListener(listener);
/*  715:     */   }
/*  716:     */   
/*  717:     */ 
/*  718:     */ 
/*  719:     */ 
/*  720:     */ 
/*  721:     */ 
/*  722:     */ 
/*  723:     */ 
/*  724:     */ 
/*  725:     */ 
/*  726:     */ 
/*  727:     */   public void setOnChildViewHolderSelectedListener(@Nullable OnChildViewHolderSelectedListener listener)
/*  728:     */   {
/*  729: 729 */     mLayoutManager.setOnChildViewHolderSelectedListener(listener);
/*  730:     */   }
/*  731:     */   
/*  732:     */ 
/*  733:     */ 
/*  734:     */ 
/*  735:     */ 
/*  736:     */ 
/*  737:     */ 
/*  738:     */ 
/*  739:     */ 
/*  740:     */   public void addOnChildViewHolderSelectedListener(@NonNull OnChildViewHolderSelectedListener listener)
/*  741:     */   {
/*  742: 742 */     mLayoutManager.addOnChildViewHolderSelectedListener(listener);
/*  743:     */   }
/*  744:     */   
/*  745:     */ 
/*  746:     */ 
/*  747:     */ 
/*  748:     */ 
/*  749:     */ 
/*  750:     */   public void removeOnChildViewHolderSelectedListener(@NonNull OnChildViewHolderSelectedListener listener)
/*  751:     */   {
/*  752: 752 */     mLayoutManager.removeOnChildViewHolderSelectedListener(listener);
/*  753:     */   }
/*  754:     */   
/*  755:     */ 
/*  756:     */ 
/*  757:     */   public void setSelectedPosition(int position)
/*  758:     */   {
/*  759: 759 */     mLayoutManager.setSelection(position, 0);
/*  760:     */   }
/*  761:     */   
/*  762:     */ 
/*  763:     */ 
/*  764:     */ 
/*  765:     */   @RestrictTo({androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/*  766:     */   public void setSelectedPositionWithSub(int position, int subposition)
/*  767:     */   {
/*  768: 768 */     mLayoutManager.setSelectionWithSub(position, subposition, 0);
/*  769:     */   }
/*  770:     */   
/*  771:     */ 
/*  772:     */ 
/*  773:     */ 
/*  774:     */ 
/*  775:     */   public void setSelectedPosition(int position, int scrollExtra)
/*  776:     */   {
/*  777: 777 */     mLayoutManager.setSelection(position, scrollExtra);
/*  778:     */   }
/*  779:     */   
/*  780:     */ 
/*  781:     */ 
/*  782:     */ 
/*  783:     */ 
/*  784:     */ 
/*  785:     */   @RestrictTo({androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/*  786:     */   public void setSelectedPositionWithSub(int position, int subposition, int scrollExtra)
/*  787:     */   {
/*  788: 788 */     mLayoutManager.setSelectionWithSub(position, subposition, scrollExtra);
/*  789:     */   }
/*  790:     */   
/*  791:     */ 
/*  792:     */ 
/*  793:     */ 
/*  794:     */ 
/*  795:     */ 
/*  796:     */   public void setSelectedPositionSmooth(int position)
/*  797:     */   {
/*  798: 798 */     mLayoutManager.setSelectionSmooth(position);
/*  799:     */   }
/*  800:     */   
/*  801:     */ 
/*  802:     */ 
/*  803:     */ 
/*  804:     */ 
/*  805:     */   @RestrictTo({androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/*  806:     */   public void setSelectedPositionSmoothWithSub(int position, int subposition)
/*  807:     */   {
/*  808: 808 */     mLayoutManager.setSelectionSmoothWithSub(position, subposition);
/*  809:     */   }
/*  810:     */   
/*  811:     */ 
/*  812:     */ 
/*  813:     */ 
/*  814:     */ 
/*  815:     */ 
/*  816:     */ 
/*  817:     */   public void setSelectedPositionSmooth(final int position, @Nullable final ViewHolderTask task)
/*  818:     */   {
/*  819: 819 */     if (task != null) {
/*  820: 820 */       RecyclerView.ViewHolder vh = findViewHolderForPosition(position);
/*  821: 821 */       if ((vh == null) || (hasPendingAdapterUpdates())) {
/*  822: 822 */         addOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener()
/*  823:     */         {
/*  824:     */           public void onChildViewHolderSelected(@NonNull RecyclerView parent, RecyclerView.ViewHolder child, int selectedPosition, int subposition)
/*  825:     */           {
/*  826: 826 */             if (selectedPosition == position) {
/*  827: 827 */               removeOnChildViewHolderSelectedListener(this);
/*  828: 828 */               task.run(child);
/*  829:     */             }
/*  830:     */           }
/*  831:     */         });
/*  832:     */       } else {
/*  833: 833 */         task.run(vh);
/*  834:     */       }
/*  835:     */     }
/*  836: 836 */     setSelectedPositionSmooth(position);
/*  837:     */   }
/*  838:     */   
/*  839:     */ 
/*  840:     */ 
/*  841:     */ 
/*  842:     */ 
/*  843:     */ 
/*  844:     */ 
/*  845:     */   public void setSelectedPosition(final int position, @Nullable final ViewHolderTask task)
/*  846:     */   {
/*  847: 847 */     if (task != null) {
/*  848: 848 */       RecyclerView.ViewHolder vh = findViewHolderForPosition(position);
/*  849: 849 */       if ((vh == null) || (hasPendingAdapterUpdates())) {
/*  850: 850 */         addOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener()
/*  851:     */         {
/*  852:     */           public void onChildViewHolderSelectedAndPositioned(@NonNull RecyclerView parent, RecyclerView.ViewHolder child, int selectedPosition, int subposition)
/*  853:     */           {
/*  854: 854 */             if (selectedPosition == position) {
/*  855: 855 */               removeOnChildViewHolderSelectedListener(this);
/*  856: 856 */               task.run(child);
/*  857:     */             }
/*  858:     */           }
/*  859:     */         });
/*  860:     */       } else {
/*  861: 861 */         task.run(vh);
/*  862:     */       }
/*  863:     */     }
/*  864: 864 */     setSelectedPosition(position);
/*  865:     */   }
/*  866:     */   
/*  867:     */ 
/*  868:     */ 
/*  869:     */ 
/*  870:     */ 
/*  871:     */   public int getSelectedPosition()
/*  872:     */   {
/*  873: 873 */     return mLayoutManager.getSelection();
/*  874:     */   }
/*  875:     */   
/*  876:     */ 
/*  877:     */ 
/*  878:     */ 
/*  879:     */ 
/*  880:     */ 
/*  881:     */ 
/*  882:     */   @RestrictTo({androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/*  883:     */   public int getSelectedSubPosition()
/*  884:     */   {
/*  885: 885 */     return mLayoutManager.getSubSelection();
/*  886:     */   }
/*  887:     */   
/*  888:     */ 
/*  889:     */ 
/*  890:     */ 
/*  891:     */ 
/*  892:     */ 
/*  893:     */   public void setAnimateChildLayout(boolean animateChildLayout)
/*  894:     */   {
/*  895: 895 */     if (mAnimateChildLayout != animateChildLayout) {
/*  896: 896 */       mAnimateChildLayout = animateChildLayout;
/*  897: 897 */       if (!mAnimateChildLayout) {
/*  898: 898 */         mSavedItemAnimator = getItemAnimator();
/*  899: 899 */         super.setItemAnimator(null);
/*  900:     */       } else {
/*  901: 901 */         super.setItemAnimator(mSavedItemAnimator);
/*  902:     */       }
/*  903:     */     }
/*  904:     */   }
/*  905:     */   
/*  906:     */ 
/*  907:     */ 
/*  908:     */ 
/*  909:     */ 
/*  910:     */ 
/*  911:     */   public boolean isChildLayoutAnimated()
/*  912:     */   {
/*  913: 913 */     return mAnimateChildLayout;
/*  914:     */   }
/*  915:     */   
/*  916:     */ 
/*  917:     */ 
/*  918:     */ 
/*  919:     */ 
/*  920:     */ 
/*  921:     */   public void setGravity(int gravity)
/*  922:     */   {
/*  923: 923 */     mLayoutManager.setGravity(gravity);
/*  924: 924 */     requestLayout();
/*  925:     */   }
/*  926:     */   
/*  927:     */   public void setLayoutManager(@Nullable RecyclerView.LayoutManager layout)
/*  928:     */   {
/*  929: 929 */     if (layout == null) {
/*  930: 930 */       super.setLayoutManager(null);
/*  931: 931 */       if (mLayoutManager != null) {
/*  932: 932 */         mLayoutManager.setGridView(null);
/*  933:     */       }
/*  934: 934 */       mLayoutManager = null;
/*  935: 935 */       return;
/*  936:     */     }
/*  937:     */     
/*  938: 938 */     mLayoutManager = ((GridLayoutManager)layout);
/*  939: 939 */     mLayoutManager.setGridView(this);
/*  940: 940 */     super.setLayoutManager(layout);
/*  941:     */   }
/*  942:     */   
/*  943:     */ 
/*  944:     */   public boolean onRequestFocusInDescendants(int direction, @Nullable Rect previouslyFocusedRect)
/*  945:     */   {
/*  946: 946 */     if ((mPrivateFlag & 0x1) == 1)
/*  947:     */     {
/*  948: 948 */       return false;
/*  949:     */     }
/*  950: 950 */     return mLayoutManager.gridOnRequestFocusInDescendants(this, direction, previouslyFocusedRect);
/*  951:     */   }
/*  952:     */   
/*  953:     */ 
/*  954:     */ 
/*  955:     */ 
/*  956:     */ 
/*  957:     */ 
/*  958:     */ 
/*  959:     */ 
/*  960:     */   public void getViewSelectedOffsets(@NonNull View view, @NonNull int[] offsets)
/*  961:     */   {
/*  962: 962 */     mLayoutManager.getViewSelectedOffsets(view, offsets);
/*  963:     */   }
/*  964:     */   
/*  965:     */   public int getChildDrawingOrder(int childCount, int i)
/*  966:     */   {
/*  967: 967 */     return mLayoutManager.getChildDrawingOrder(this, childCount, i);
/*  968:     */   }
/*  969:     */   
/*  970:     */   final boolean isChildrenDrawingOrderEnabledInternal() {
/*  971: 971 */     return isChildrenDrawingOrderEnabled();
/*  972:     */   }
/*  973:     */   
/*  974:     */   @Nullable
/*  975:     */   public View focusSearch(int direction)
/*  976:     */   {
/*  977: 977 */     if (isFocused())
/*  978:     */     {
/*  979:     */ 
/*  980: 980 */       View view = mLayoutManager.findViewByPosition(mLayoutManager.getSelection());
/*  981: 981 */       if (view != null) {
/*  982: 982 */         return focusSearch(view, direction);
/*  983:     */       }
/*  984:     */     }
/*  985:     */     
/*  986: 986 */     return super.focusSearch(direction);
/*  987:     */   }
/*  988:     */   
/*  989:     */ 
/*  990:     */   protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect)
/*  991:     */   {
/*  992: 992 */     super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
/*  993: 993 */     mLayoutManager.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
/*  994:     */   }
/*  995:     */   
/*  996:     */ 
/*  997:     */ 
/*  998:     */ 
/*  999:     */ 
/* 1000:     */ 
/* 1001:     */ 
/* 1002:     */   public final void setFocusSearchDisabled(boolean disabled)
/* 1003:     */   {
/* 1004:1004 */     setDescendantFocusability(disabled ? 393216 : 262144);
/* 1005:1005 */     mLayoutManager.setFocusSearchDisabled(disabled);
/* 1006:     */   }
/* 1007:     */   
/* 1008:     */ 
/* 1009:     */ 
/* 1010:     */ 
/* 1011:     */ 
/* 1012:     */   public final boolean isFocusSearchDisabled()
/* 1013:     */   {
/* 1014:1014 */     return mLayoutManager.isFocusSearchDisabled();
/* 1015:     */   }
/* 1016:     */   
/* 1017:     */ 
/* 1018:     */ 
/* 1019:     */ 
/* 1020:     */ 
/* 1021:     */ 
/* 1022:     */   public void setLayoutEnabled(boolean layoutEnabled)
/* 1023:     */   {
/* 1024:1024 */     mLayoutManager.setLayoutEnabled(layoutEnabled);
/* 1025:     */   }
/* 1026:     */   
/* 1027:     */ 
/* 1028:     */ 
/* 1029:     */ 
/* 1030:     */ 
/* 1031:     */   public void setChildrenVisibility(int visibility)
/* 1032:     */   {
/* 1033:1033 */     mLayoutManager.setChildrenVisibility(visibility);
/* 1034:     */   }
/* 1035:     */   
/* 1036:     */ 
/* 1037:     */ 
/* 1038:     */ 
/* 1039:     */ 
/* 1040:     */   public void setPruneChild(boolean pruneChild)
/* 1041:     */   {
/* 1042:1042 */     mLayoutManager.setPruneChild(pruneChild);
/* 1043:     */   }
/* 1044:     */   
/* 1045:     */ 
/* 1046:     */ 
/* 1047:     */ 
/* 1048:     */ 
/* 1049:     */   public void setScrollEnabled(boolean scrollEnabled)
/* 1050:     */   {
/* 1051:1051 */     mLayoutManager.setScrollEnabled(scrollEnabled);
/* 1052:     */   }
/* 1053:     */   
/* 1054:     */ 
/* 1055:     */ 
/* 1056:     */ 
/* 1057:     */ 
/* 1058:     */   public boolean isScrollEnabled()
/* 1059:     */   {
/* 1060:1060 */     return mLayoutManager.isScrollEnabled();
/* 1061:     */   }
/* 1062:     */   
/* 1063:     */ 
/* 1064:     */ 
/* 1065:     */ 
/* 1066:     */ 
/* 1067:     */ 
/* 1068:     */ 
/* 1069:     */   public boolean hasPreviousViewInSameRow(int position)
/* 1070:     */   {
/* 1071:1071 */     return mLayoutManager.hasPreviousViewInSameRow(position);
/* 1072:     */   }
/* 1073:     */   
/* 1074:     */ 
/* 1075:     */ 
/* 1076:     */ 
/* 1077:     */ 
/* 1078:     */   public void setFocusDrawingOrderEnabled(boolean enabled)
/* 1079:     */   {
/* 1080:1080 */     super.setChildrenDrawingOrderEnabled(enabled);
/* 1081:     */   }
/* 1082:     */   
/* 1083:     */ 
/* 1084:     */ 
/* 1085:     */ 
/* 1086:     */ 
/* 1087:     */   public boolean isFocusDrawingOrderEnabled()
/* 1088:     */   {
/* 1089:1089 */     return super.isChildrenDrawingOrderEnabled();
/* 1090:     */   }
/* 1091:     */   
/* 1092:     */ 
/* 1093:     */ 
/* 1094:     */ 
/* 1095:     */ 
/* 1096:     */   public void setOnTouchInterceptListener(@Nullable OnTouchInterceptListener listener)
/* 1097:     */   {
/* 1098:1098 */     mOnTouchInterceptListener = listener;
/* 1099:     */   }
/* 1100:     */   
/* 1101:     */ 
/* 1102:     */ 
/* 1103:     */ 
/* 1104:     */ 
/* 1105:     */   public void setOnMotionInterceptListener(@Nullable OnMotionInterceptListener listener)
/* 1106:     */   {
/* 1107:1107 */     mOnMotionInterceptListener = listener;
/* 1108:     */   }
/* 1109:     */   
/* 1110:     */ 
/* 1111:     */ 
/* 1112:     */ 
/* 1113:     */ 
/* 1114:     */   public void setOnKeyInterceptListener(@Nullable OnKeyInterceptListener listener)
/* 1115:     */   {
/* 1116:1116 */     mOnKeyInterceptListener = listener;
/* 1117:     */   }
/* 1118:     */   
/* 1119:     */ 
/* 1120:     */ 
/* 1121:     */ 
/* 1122:     */ 
/* 1123:     */   public void setOnUnhandledKeyListener(@Nullable OnUnhandledKeyListener listener)
/* 1124:     */   {
/* 1125:1125 */     mOnUnhandledKeyListener = listener;
/* 1126:     */   }
/* 1127:     */   
/* 1128:     */ 
/* 1129:     */ 
/* 1130:     */ 
/* 1131:     */ 
/* 1132:     */   @Nullable
/* 1133:     */   public OnUnhandledKeyListener getOnUnhandledKeyListener()
/* 1134:     */   {
/* 1135:1135 */     return mOnUnhandledKeyListener;
/* 1136:     */   }
/* 1137:     */   
/* 1138:     */   public boolean dispatchKeyEvent(@NonNull KeyEvent event)
/* 1139:     */   {
/* 1140:1140 */     if ((mOnKeyInterceptListener != null) && (mOnKeyInterceptListener.onInterceptKeyEvent(event))) {
/* 1141:1141 */       return true;
/* 1142:     */     }
/* 1143:1143 */     if (super.dispatchKeyEvent(event)) {
/* 1144:1144 */       return true;
/* 1145:     */     }
/* 1146:1146 */     return (mOnUnhandledKeyListener != null) && (mOnUnhandledKeyListener.onUnhandledKey(event));
/* 1147:     */   }
/* 1148:     */   
/* 1149:     */   public boolean dispatchTouchEvent(@NonNull MotionEvent event)
/* 1150:     */   {
/* 1151:1151 */     if ((mOnTouchInterceptListener != null) && 
/* 1152:1152 */       (mOnTouchInterceptListener.onInterceptTouchEvent(event))) {
/* 1153:1153 */       return true;
/* 1154:     */     }
/* 1155:     */     
/* 1156:1156 */     return super.dispatchTouchEvent(event);
/* 1157:     */   }
/* 1158:     */   
/* 1159:     */   protected boolean dispatchGenericFocusedEvent(@NonNull MotionEvent event)
/* 1160:     */   {
/* 1161:1161 */     if ((mOnMotionInterceptListener != null) && 
/* 1162:1162 */       (mOnMotionInterceptListener.onInterceptMotionEvent(event))) {
/* 1163:1163 */       return true;
/* 1164:     */     }
/* 1165:     */     
/* 1166:1166 */     return super.dispatchGenericFocusedEvent(event);
/* 1167:     */   }
/* 1168:     */   
/* 1169:     */ 
/* 1170:     */ 
/* 1171:     */ 
/* 1172:     */ 
/* 1173:     */ 
/* 1174:     */   public final int getSaveChildrenPolicy()
/* 1175:     */   {
/* 1176:1176 */     return mLayoutManager.mChildrenStates.getSavePolicy();
/* 1177:     */   }
/* 1178:     */   
/* 1179:     */ 
/* 1180:     */ 
/* 1181:     */ 
/* 1182:     */   public final int getSaveChildrenLimitNumber()
/* 1183:     */   {
/* 1184:1184 */     return mLayoutManager.mChildrenStates.getLimitNumber();
/* 1185:     */   }
/* 1186:     */   
/* 1187:     */ 
/* 1188:     */ 
/* 1189:     */ 
/* 1190:     */ 
/* 1191:     */ 
/* 1192:     */   public final void setSaveChildrenPolicy(int savePolicy)
/* 1193:     */   {
/* 1194:1194 */     mLayoutManager.mChildrenStates.setSavePolicy(savePolicy);
/* 1195:     */   }
/* 1196:     */   
/* 1197:     */ 
/* 1198:     */ 
/* 1199:     */   public final void setSaveChildrenLimitNumber(int limitNumber)
/* 1200:     */   {
/* 1201:1201 */     mLayoutManager.mChildrenStates.setLimitNumber(limitNumber);
/* 1202:     */   }
/* 1203:     */   
/* 1204:     */   public boolean hasOverlappingRendering()
/* 1205:     */   {
/* 1206:1206 */     return mHasOverlappingRendering;
/* 1207:     */   }
/* 1208:     */   
/* 1209:     */   public void setHasOverlappingRendering(boolean hasOverlapping) {
/* 1210:1210 */     mHasOverlappingRendering = hasOverlapping;
/* 1211:     */   }
/* 1212:     */   
/* 1213:     */ 
/* 1214:     */ 
/* 1215:     */ 
/* 1216:     */   public void onRtlPropertiesChanged(int layoutDirection)
/* 1217:     */   {
/* 1218:1218 */     if (mLayoutManager != null) {
/* 1219:1219 */       mLayoutManager.onRtlPropertiesChanged(layoutDirection);
/* 1220:     */     }
/* 1221:     */   }
/* 1222:     */   
/* 1223:     */ 
/* 1224:     */ 
/* 1225:     */ 
/* 1226:     */ 
/* 1227:     */ 
/* 1228:     */   @RestrictTo({androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/* 1229:     */   public void setExtraLayoutSpace(int extraLayoutSpace)
/* 1230:     */   {
/* 1231:1231 */     mLayoutManager.setExtraLayoutSpace(extraLayoutSpace);
/* 1232:     */   }
/* 1233:     */   
/* 1234:     */ 
/* 1235:     */ 
/* 1236:     */ 
/* 1237:     */   @RestrictTo({androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/* 1238:     */   public int getExtraLayoutSpace()
/* 1239:     */   {
/* 1240:1240 */     return mLayoutManager.getExtraLayoutSpace();
/* 1241:     */   }
/* 1242:     */   
/* 1243:     */ 
/* 1244:     */ 
/* 1245:     */ 
/* 1246:     */ 
/* 1247:     */   public void animateOut()
/* 1248:     */   {
/* 1249:1249 */     mLayoutManager.slideOut();
/* 1250:     */   }
/* 1251:     */   
/* 1252:     */ 
/* 1253:     */ 
/* 1254:     */   public void animateIn()
/* 1255:     */   {
/* 1256:1256 */     mLayoutManager.slideIn();
/* 1257:     */   }
/* 1258:     */   
/* 1259:     */ 
/* 1260:     */   public void scrollToPosition(int position)
/* 1261:     */   {
/* 1262:1262 */     if (mLayoutManager.isSlidingChildViews()) {
/* 1263:1263 */       mLayoutManager.setSelectionWithSub(position, 0, 0);
/* 1264:1264 */       return;
/* 1265:     */     }
/* 1266:1266 */     super.scrollToPosition(position);
/* 1267:     */   }
/* 1268:     */   
/* 1269:     */ 
/* 1270:     */   public void smoothScrollToPosition(int position)
/* 1271:     */   {
/* 1272:1272 */     if (mLayoutManager.isSlidingChildViews()) {
/* 1273:1273 */       mLayoutManager.setSelectionWithSub(position, 0, 0);
/* 1274:1274 */       return;
/* 1275:     */     }
/* 1276:1276 */     super.smoothScrollToPosition(position);
/* 1277:     */   }
/* 1278:     */   
/* 1279:     */ 
/* 1280:     */ 
/* 1281:     */ 
/* 1282:     */ 
/* 1283:     */   public final void setSmoothScrollByBehavior(@Nullable SmoothScrollByBehavior behavior)
/* 1284:     */   {
/* 1285:1285 */     mSmoothScrollByBehavior = behavior;
/* 1286:     */   }
/* 1287:     */   
/* 1288:     */ 
/* 1289:     */ 
/* 1290:     */ 
/* 1291:     */ 
/* 1292:     */   @Nullable
/* 1293:     */   public SmoothScrollByBehavior getSmoothScrollByBehavior()
/* 1294:     */   {
/* 1295:1295 */     return mSmoothScrollByBehavior;
/* 1296:     */   }
/* 1297:     */   
/* 1298:     */   public void smoothScrollBy(int dx, int dy)
/* 1299:     */   {
/* 1300:1300 */     if (mSmoothScrollByBehavior != null) {
/* 1301:1301 */       smoothScrollBy(dx, dy, mSmoothScrollByBehavior
/* 1302:1302 */         .configSmoothScrollByInterpolator(dx, dy), mSmoothScrollByBehavior
/* 1303:1303 */         .configSmoothScrollByDuration(dx, dy));
/* 1304:     */     } else {
/* 1305:1305 */       smoothScrollBy(dx, dy, null, -2147483648);
/* 1306:     */     }
/* 1307:     */   }
/* 1308:     */   
/* 1309:     */   public void smoothScrollBy(int dx, int dy, @Nullable Interpolator interpolator)
/* 1310:     */   {
/* 1311:1311 */     if (mSmoothScrollByBehavior != null) {
/* 1312:1312 */       smoothScrollBy(dx, dy, interpolator, mSmoothScrollByBehavior
/* 1313:     */       
/* 1314:1314 */         .configSmoothScrollByDuration(dx, dy));
/* 1315:     */     } else {
/* 1316:1316 */       smoothScrollBy(dx, dy, interpolator, -2147483648);
/* 1317:     */     }
/* 1318:     */   }
/* 1319:     */   
/* 1320:     */ 
/* 1321:     */ 
/* 1322:     */ 
/* 1323:     */ 
/* 1324:     */ 
/* 1325:     */   public final void setSmoothScrollSpeedFactor(float smoothScrollSpeedFactor)
/* 1326:     */   {
/* 1327:1327 */     mLayoutManager.mSmoothScrollSpeedFactor = smoothScrollSpeedFactor;
/* 1328:     */   }
/* 1329:     */   
/* 1330:     */ 
/* 1331:     */ 
/* 1332:     */   public final float getSmoothScrollSpeedFactor()
/* 1333:     */   {
/* 1334:1334 */     return mLayoutManager.mSmoothScrollSpeedFactor;
/* 1335:     */   }
/* 1336:     */   
/* 1337:     */ 
/* 1338:     */ 
/* 1339:     */ 
/* 1340:     */ 
/* 1341:     */ 
/* 1342:     */ 
/* 1343:     */ 
/* 1344:     */ 
/* 1345:     */ 
/* 1346:     */   public final void setSmoothScrollMaxPendingMoves(int maxPendingMoves)
/* 1347:     */   {
/* 1348:1348 */     mLayoutManager.mMaxPendingMoves = maxPendingMoves;
/* 1349:     */   }
/* 1350:     */   
/* 1351:     */ 
/* 1352:     */ 
/* 1353:     */ 
/* 1354:     */ 
/* 1355:     */ 
/* 1356:     */ 
/* 1357:     */ 
/* 1358:     */ 
/* 1359:     */ 
/* 1360:     */ 
/* 1361:     */   public final int getSmoothScrollMaxPendingMoves()
/* 1362:     */   {
/* 1363:1363 */     return mLayoutManager.mMaxPendingMoves;
/* 1364:     */   }
/* 1365:     */   
/* 1366:     */ 
/* 1367:     */ 
/* 1368:     */ 
/* 1369:     */ 
/* 1370:     */ 
/* 1371:     */ 
/* 1372:     */ 
/* 1373:     */ 
/* 1374:     */ 
/* 1375:     */ 
/* 1376:     */ 
/* 1377:     */ 
/* 1378:     */ 
/* 1379:     */ 
/* 1380:     */ 
/* 1381:     */ 
/* 1382:     */ 
/* 1383:     */ 
/* 1384:     */ 
/* 1385:     */ 
/* 1386:     */ 
/* 1387:     */ 
/* 1388:     */ 
/* 1389:     */ 
/* 1390:     */ 
/* 1391:     */ 
/* 1392:     */ 
/* 1393:     */ 
/* 1394:     */   public void setInitialPrefetchItemCount(int itemCount)
/* 1395:     */   {
/* 1396:1396 */     mInitialPrefetchItemCount = itemCount;
/* 1397:     */   }
/* 1398:     */   
/* 1399:     */ 
/* 1400:     */ 
/* 1401:     */ 
/* 1402:     */ 
/* 1403:     */ 
/* 1404:     */ 
/* 1405:     */ 
/* 1406:     */ 
/* 1407:     */ 
/* 1408:     */ 
/* 1409:     */ 
/* 1410:     */   public int getInitialPrefetchItemCount()
/* 1411:     */   {
/* 1412:1412 */     return mInitialPrefetchItemCount;
/* 1413:     */   }
/* 1414:     */   
/* 1415:     */   public void removeView(@NonNull View view)
/* 1416:     */   {
/* 1417:1417 */     boolean retainFocusForChild = (view.hasFocus()) && (isFocusable());
/* 1418:1418 */     if (retainFocusForChild)
/* 1419:     */     {
/* 1420:     */ 
/* 1421:1421 */       mPrivateFlag |= 0x1;
/* 1422:1422 */       requestFocus();
/* 1423:     */     }
/* 1424:1424 */     super.removeView(view);
/* 1425:1425 */     if (retainFocusForChild) {
/* 1426:1426 */       mPrivateFlag ^= 0xFFFFFFFE;
/* 1427:     */     }
/* 1428:     */   }
/* 1429:     */   
/* 1430:     */   public void removeViewAt(int index)
/* 1431:     */   {
/* 1432:1432 */     boolean retainFocusForChild = getChildAt(index).hasFocus();
/* 1433:1433 */     if (retainFocusForChild)
/* 1434:     */     {
/* 1435:     */ 
/* 1436:1436 */       mPrivateFlag |= 0x1;
/* 1437:1437 */       requestFocus();
/* 1438:     */     }
/* 1439:1439 */     super.removeViewAt(index);
/* 1440:1440 */     if (retainFocusForChild) {
/* 1441:1441 */       mPrivateFlag ^= 0xFFFFFFFE;
/* 1442:     */     }
/* 1443:     */   }
/* 1444:     */   
/* 1445:     */   public static abstract interface OnLayoutCompletedListener
/* 1446:     */   {
/* 1447:     */     public abstract void onLayoutCompleted(@NonNull RecyclerView.State paramState);
/* 1448:     */   }
/* 1449:     */   
/* 1450:     */   public static abstract interface OnTouchInterceptListener
/* 1451:     */   {
/* 1452:     */     public abstract boolean onInterceptTouchEvent(@NonNull MotionEvent paramMotionEvent);
/* 1453:     */   }
/* 1454:     */   
/* 1455:     */   public static abstract interface OnMotionInterceptListener
/* 1456:     */   {
/* 1457:     */     public abstract boolean onInterceptMotionEvent(@NonNull MotionEvent paramMotionEvent);
/* 1458:     */   }
/* 1459:     */   
/* 1460:     */   public static abstract interface OnKeyInterceptListener
/* 1461:     */   {
/* 1462:     */     public abstract boolean onInterceptKeyEvent(@NonNull KeyEvent paramKeyEvent);
/* 1463:     */   }
/* 1464:     */   
/* 1465:     */   public static abstract interface OnUnhandledKeyListener
/* 1466:     */   {
/* 1467:     */     public abstract boolean onUnhandledKey(@NonNull KeyEvent paramKeyEvent);
/* 1468:     */   }
/* 1469:     */   
/* 1470:     */   public static abstract interface SmoothScrollByBehavior
/* 1471:     */   {
/* 1472:     */     public abstract int configSmoothScrollByDuration(int paramInt1, int paramInt2);
/* 1473:     */     
/* 1474:     */     @Nullable
/* 1475:     */     public abstract Interpolator configSmoothScrollByInterpolator(int paramInt1, int paramInt2);
/* 1476:     */   }
/* 1477:     */ }


/* Location:           D:\Android\Huan_Component\androidx_aar_leanback\repo\classes.jar
 * Qualified Name:     androidx.leanback.widget.BaseGridView
 * JD-Core Version:    0.7.0.1
 */