{-
   test4.hs
-}
test4 = 
  let 
    x = 5
    y = 6
  in 
    let 
      x = 6
      z = 7
    in 
      let f x = x + y + z
      in f 2
{-
   end test4
-}
