{-
   test3.hs
-}
test3 = 
  let 
    x = 4 + 3
    y = 9 * 2 - 10
    r = x:y:[]
  in 
    head(r) + head(tail(r))
{-
   end test3
-}

